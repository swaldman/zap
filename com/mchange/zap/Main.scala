package com.mchange.zap

import com.mchange.zap.webfinger.Jrd

import sttp.model.StatusCode
import sttp.tapir.{Endpoint,EndpointOutput}
import sttp.tapir.ztapir.*
import sttp.tapir.json.upickle.*
import sttp.tapir.generic.auto.*

import zio.*
import zio.http.{Http, HttpApp, Request, Response, Server}
import sttp.tapir.server.ziohttp.ZioHttpInterpreter

object Main extends ZIOAppDefault:

  val AcctPrefix = "acct:"
  val AcctPrefixLen = AcctPrefix.length

  override def run =
    val webFingerEndpoint : Endpoint[Unit,String,String,String,Any] =
      endpoint
        .get
        .in(".well-known" / "webfinger")
        .in(query[String]("resource"))
        .out(stringJsonBody)
        .out( header("Content-Type", "application/jrd+json") )
        .errorOut(statusCode(StatusCode.NotFound).and(stringBody))
    val logic : String => ZIO[Any,String,String] = resource =>
      val baseLogic =
        if resource.startsWith(AcctPrefix) then
          val acctName = resource.drop(AcctPrefixLen)
          webfinger.Source.Dummy.recordForAccount(acctName)
        else
          ZIO.succeed(None : Option[Jrd])
      baseLogic.orDie.flatMap:
        case Some(jrd) => ZIO.succeed(upickle.default.write(jrd))
        case None      => ZIO.fail(s"Resource '${resource}' not found.")
    val webfingerServerEndpoint = webFingerEndpoint.zServerLogic[Any](logic)
    val httpApp = ZioHttpInterpreter().toHttp(webfingerServerEndpoint)
    val serve =
      Server
        .serve( httpApp.withDefaultErrorResponse )
        .provide(ZLayer.succeed(Server.Config.default.port(9123)),Server.live)
        .exitCode
    serve
