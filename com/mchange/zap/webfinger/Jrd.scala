package com.mchange.zap.webfinger

import com.mchange.zap.util.*

import upickle.default.ReadWriter

object Jrd:
  object Link:
    given ReadWriter[Jrd.Link] = upickle.default.readwriter[ujson.Value].bimap[Jrd.Link](toUJsonAst,fromUJsonAst)
    def toUJsonAst( link : Link ) : ujson.Value =
      val items =
        val builder = Seq.newBuilder[(String,ujson.Value)]
        builder += Tuple2("rel", ujson.Str(link.rel))
        link.href.foreach( href => builder += Tuple2("href", href) )
        link.`type`.foreach( `type` => builder += Tuple2("type", `type`) )
        if link.titles.nonEmpty then
          val obj = ujsonAstFromMapStringString(link.titles)
          builder += Tuple2("titles", obj)
        link.template.foreach( template => builder += Tuple2("template", template) )
        builder.result()
      ujson.Obj( items.head, items.tail* )
    def fromUJsonAst( value : ujson.Value ) : Link =
      val obj      = value.obj
      val rel      = obj("rel").str
      val href     = obj.get("href").map( _.str )
      val `type`   = obj.get("type").map( _.str )
      val titles   = obj.get("titles").fold(Map.empty)(v => v.obj.map((k,v)=>(k,v.str)).toMap)
      val template = obj.get("template").map( _.str )
      Link(rel, href, `type`, titles, template)
  case class Link(
    rel : String,
    href : Option[String],
    `type` : Option[String],
    titles : Map[String,String],
    template : Option[String]
  )
  given ReadWriter[Jrd] = upickle.default.readwriter[ujson.Value].bimap[Jrd](toUJsonAst,fromUJsonAst)
  def toUJsonAst( jrd : Jrd ) : ujson.Value =
    val items =
      val builder = Seq.newBuilder[(String,ujson.Value)]
      builder += Tuple2("subject", ujson.Str(jrd.subject))
      if jrd.aliases.nonEmpty then
        val alArr = ujsonAstFromSeqString(jrd.aliases)
        builder += Tuple2("aliases", alArr)
      if jrd.properties.nonEmpty then
        val props = ujsonAstFromMapStringString(jrd.properties)
        builder += Tuple2("properties", props)
      if jrd.links.nonEmpty then
        val links = ujson.Arr( jrd.links.map(Jrd.Link.toUJsonAst)* )
        builder += Tuple2("links", links)
      builder.result()
    ujson.Obj( items.head, items.tail* )
  def fromUJsonAst( value : ujson.Value ) : Jrd =
    val obj        = value.obj
    val subject    = obj("subject").str
    val aliases    = obj.get("aliases").fold(Seq.empty)(v => v.arr.map(_.str).toSeq)
    val properties = obj.get("properties").fold(Map.empty)(v => v.obj.map((k,v)=>(k,v.str)).toMap)
    val links      = obj.get("links").fold(Seq.empty)(v => v.arr.map( Jrd.Link.fromUJsonAst ).toSeq)
    Jrd(subject, aliases, properties, links)
  val Example = Jrd("acct:interfluidity@test.zap.mchange.com", Seq.empty, Map.empty, Seq.empty)
case class Jrd (
  subject    : String,
  aliases    : Seq[String],
  properties : Map[String,String],
  links      : Seq[Jrd.Link]
)
