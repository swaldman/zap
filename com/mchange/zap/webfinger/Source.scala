package com.mchange.zap.webfinger

import zio.{Task,ZIO}

object Source:
  private val DummyRecordText =
    """
    {
      "subject": "acct:interfluidity@econtwitter.net",
      "aliases": [
        "https://econtwitter.net/@interfluidity",
        "https://econtwitter.net/users/interfluidity"
      ],
      "links": [
        {
          "rel": "http://webfinger.net/rel/profile-page",
          "type": "text/html",
          "href": "https://econtwitter.net/@interfluidity"
        },
        {
          "rel": "self",
          "type": "application/activity+json",
          "href": "https://econtwitter.net/users/interfluidity"
        },
        {
          "rel": "http://ostatus.org/schema/1.0/subscribe",
          "template": "https://econtwitter.net/authorize_interaction?uri={uri}"
        }
      ]
    }""".trim
  private val DummyRecord = ujson.read(DummyRecordText).asInstanceOf[ujson.Obj]
  val Dummy = new Source:
    def recordForAccount( account : String ) : Task[Option[ujson.Obj]] = ZIO.attempt:
      account match
        case "interfluidity@test.zap.mchange.com" => Some(DummyRecord)
        case "interfluidity@econtwitter.net"      => Some(DummyRecord)
        case _                                    => None

trait Source:
  def recordForAccount( account : String ) : Task[Option[ujson.Obj]]

