package com.mchange.zap.util

def ujsonAstFromSeqString( ss : Seq[String] ) : ujson.Arr = ujson.Arr( ss.map( ujson.Str.apply )* )
def ujsonAstFromMapStringString( mss : Map[String,String] ) : ujson.Obj =
  if mss.isEmpty then
    ujson.Obj()
  else
    val tups = mss.map( (k,v) => (k,ujson.Str(v)) ).toSeq
    ujson.Obj( tups.head )
