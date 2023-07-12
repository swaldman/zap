package com.mchange.zap.webfinger

import zio.{Task,ZIO}

object Source:
  private val DummyRecordText =
    """
    {
      "subject": "acct:interfluidity@test.zap.mchange.com",
      "aliases": [
        "https://test.zap.mchange.com/@interfluidity",
        "https://test.zap.mchange.com/users/interfluidity"
      ],
      "links": [
        {
          "rel": "http://webfinger.net/rel/profile-page",
          "type": "text/html",
          "href": "https://test.zap.mchange.com/@interfluidity"
        },
        {
          "rel": "self",
          "type": "application/activity+json",
          "href": "https://test.zap.mchange.com/users/interfluidity"
        },
        {
          "rel": "http://ostatus.org/schema/1.0/subscribe",
          "template": "https://test.zap.mchange.com/authorize_interaction?uri={uri}"
        }
      ]
    }""".trim
  private val DummyRecord = ujson.read(DummyRecordText).asInstanceOf[ujson.Obj]
  val Dummy = new Source:
    def recordForAccount( account : String ) : Task[Option[ujson.Obj]] = ZIO.attempt:
      account match
        case "interfluidity@test.zap.mchange.com" => Some(DummyRecord)
        case _                                    => None 

trait Source:
  def recordForAccount( account : String ) : Task[Option[ujson.Obj]]

