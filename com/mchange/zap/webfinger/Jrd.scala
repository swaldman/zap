package com.mchange.zap.webfinger

import upickle.default.ReadWriter

object Jrd:
  given ReadWriter[Jrd] = ReadWriter.derived
  object Link:
    given ReadWriter[Jrd.Link] = ReadWriter.derived
  case class Link(
    rel : String,
    href : Option[String],
    `type` : Option[String],
    titles : Option[Map[String,String]],
    template : Option[String]
  )
case class Jrd (
  subject    : String,
  aliases    : Option[Seq[String]],
  properties : Option[Map[String,String]],
  links      : Option[Seq[Jrd.Link]]
)
