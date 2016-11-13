package net.zettadata.dibbuk

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model._

import play.api.libs.json._

import scala.io.Source

class Dibbuk 
{
    val version: Double = 0.01
    var extractors: Seq[JsObject] = _    
    var doc: Document = _
    
    def setPage( url: String ): Unit = 
    {
        val browser = JsoupBrowser()
        doc = browser.get( url )
    }
        
    def setConfig( url: String ): Unit = 
    { 
        val jText = Source.fromURL( url ).mkString
        val config = Json.parse( jText ).as[JsObject]
        val v = (config \ "version").as[Double]
        if ( version < v )
        {
            println( "Dibbuk version " + version + " does not support DSDL version " + v )
            System.exit(-1)
        }
        extractors = ((config \ "extractors").as[JsArray]).as[Seq[JsObject]]
    }
    
    def run(): Map[String,Any] =
    {
        scrape( extractors ).collect { case (key, Some(value)) => key -> value }
    }
        
    def scrape( extractors: Seq[JsObject] ): Map[String,Any] =
    {
        val extractor = extractors.head
        val key = ( extractor \ "key" ).as[String]
        val command = ( extractor \ "command" ).as[String]
        val result = command match {
            case "getTag" => {
                val tag = ( extractor \ "tag" ).as[String]
                Map( key -> getTag( tag ) )
            }
            case "getTagInContext" => {
                val tag = ( extractor \ "tag" ).as[String]
                val ctx = ( extractor \ "ctx" ).as[String]
                Map( key -> getTagInContext( tag, ctx ) )
            }
            case "getTagInContexts" => {
                val tag = ( extractor \ "tag" ).as[String]
                val ctx = ( extractor \ "ctx" ).as[String]
                Map( key -> getTagInContexts( tag, ctx ) )
            }
            case "getTags" => {
                val tag = ( extractor \ "tag" ).as[String]
                Map( key -> getTags( tag ) )
            }
            case "getTagsInContext" => {
                val tag = ( extractor \ "tag" ).as[String]
                val ctx = ( extractor \ "ctx" ).as[String]
                Map( key -> getTagsInContext( tag, ctx ) )
            }
            case "getTagsInContexts" => {
                val tag = ( extractor \ "tag" ).as[String]
                val ctx = ( extractor \ "ctx" ).as[String]
                Map( key -> getTagsInContexts( tag, ctx ) )
            }
            case "getAttribute" => {
                val tag = ( extractor \ "tag" ).as[String]
                val att = ( extractor \ "att" ).as[String]
                Map( key -> getAttribute( tag, att ) )
            }
            case "getAttributeInContext" => {
                val tag = ( extractor \ "tag" ).as[String]
                val att = ( extractor \ "att" ).as[String]
                val ctx = ( extractor \ "ctx" ).as[String]
                Map( key -> getAttributeInContext( tag, att, ctx ) )
            }
            case "getAttributeInContexts" => {
                val tag = ( extractor \ "tag" ).as[String]
                val att = ( extractor \ "att" ).as[String]
                val ctx = ( extractor \ "ctx" ).as[String]
                Map( key -> getAttributeInContexts( tag, att, ctx ) )
            }
            case "getAttributes" => {
                val tag = ( extractor \ "tag" ).as[String]
                val att = ( extractor \ "att" ).as[String]
                Map( key -> getAttributes( tag, att ) )
            }
            case "getAttributesInContext" => {
                val tag = ( extractor \ "tag" ).as[String]
                val att = ( extractor \ "att" ).as[String]
                val ctx = ( extractor \ "ctx" ).as[String]
                Map( key -> getAttributesInContext( tag, att, ctx ) )
            }
            case "getAttributesInContexts" => {
                val tag = ( extractor \ "tag" ).as[String]
                val att = ( extractor \ "att" ).as[String]
                val ctx = ( extractor \ "ctx" ).as[String]
                Map( key -> getAttributesInContexts( tag, att, ctx ) )
            }
            case _ => Map( key -> "!!! command not supported !!!" )
        }
        if (extractors.tail.isEmpty)
        {
            result
        }
        else
        {
            result ++ scrape( extractors.tail )
        }
    }

    def getTag( tag: String ): String =
    {
        ( doc tryExtract text( tag ) ) match {
          case None => ""
          case Some(result) => result
        }
    }
    
    def getTagInContext( tag: String, ctx: String ): Option[String] =
    {
        ( doc tryExtract element( ctx ) tryExtract text( tag ) ).flatten
    }
    
    def getTagInContexts( tag: String, ctx: String ): Option[Iterable[String]] =
    {
        ( doc tryExtract elements( ctx ) ) match {
            case None => None
            case Some(res) => Option(( res.map( _ >?> text( tag ) ) ).flatten)
        }
    }
    
    def getTags( tag: String ): Option[Iterable[String]] =
    {
        (doc tryExtract texts( tag ))
    }
    
    def getTagsInContext( tag: String, ctx: String ): Option[Iterable[String]] =
    {
        (doc tryExtract element( ctx ) tryExtract texts( tag )).flatten
    }
    
    def getTagsInContexts( tag: String, ctx: String ): Option[Iterable[String]] =
    {
        ( doc tryExtract elements( ctx ) ) match {
            case None => None
            case Some(res) => Option( ( res.flatMap( _ >?> texts( tag ) ) ).flatten )
        }
    }
    
    def getAttribute( tag: String, att: String ): Option[String] = 
    {
        ( doc tryExtract attr( att )( tag ) )
    } 
   
    def getAttributeInContext( tag: String, att: String, ctx: String ): Option[String] = 
    {
        ( doc tryExtract element( ctx ) tryExtract attr( att )( tag ) ).flatten
    }
    
    def getAttributeInContexts( tag: String, att: String, ctx: String ): Option[Iterable[String]] = 
    {
        ( doc tryExtract elements( ctx ) ) match {
            case None => None
            case Some(res) => Option( res.flatMap( _ >?> attr( att )( tag ) ) )
        }
    }
    
    def getAttributes( tag: String, att: String ): Option[Iterable[String]] = 
    {
        ( doc tryExtract attrs( att )( tag ) )
    }
    
    def getAttributesInContext( tag: String, att: String, ctx: String ): Option[Iterable[String]] = 
    {
        ( doc tryExtract element( ctx ) tryExtract attrs( att )( tag ) ).flatten
    }
    
    def getAttributesInContexts( tag: String, att: String, ctx: String ): Option[Iterable[String]] = 
    {
        ( doc tryExtract elements( ctx ) tryExtract attrs( att )( tag ) ).flatten
    }
    
}