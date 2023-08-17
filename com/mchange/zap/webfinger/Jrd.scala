package com.mchange.zap.webfinger

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

object Jrd:
  object Link:
    given JsonValueCodec[Jrd.Link] = JsonCodecMaker.make
  case class Link(
    rel : String,
    href : Option[String],
    `type` : Option[String],
    titles : Map[String,String],
    template : Option[String]
  )
  given JsonValueCodec[Jrd] = JsonCodecMaker.make
  val Example = Jrd("acct:interfluidity@test.zap.mchange.com", Seq.empty, Map.empty, Seq.empty)
case class Jrd (
  subject    : String,
  aliases    : Seq[String],
  properties : Map[String,String],
  links      : Seq[Jrd.Link]
)
