# dibbuk
Dibbuk is a wrapper for [scala-scraper](https://github.com/ruippeixotog/scala-scraper).
It permits to define simple web scrapers by writing scraping instructions in a configuration file.

## Usage
```scala
import net.zettadata.dibbuk._

// instanciate dibbuk
val dibbuk = new Dibbuk()

// provide configuration file
scrapper.setConfig( "http://somewhere.com/scraper.json" )

// set the page to scape
scrapper.setPage( "http://somewhereelse.com/pageToScrape.html" )

// run dibbuk
val result = dibbuk.run()

// results consists of a Map[String, Any]
println( result )
```

##Dibbuk Scraper Description Language (DSDL)
Configuration files are written in JSON.  As shown on the example below, they are used to configure "extractors".
For each extractor, one defines:
+ A key: Corresponding to the key under which the result of the extractor will be stored in the resulting Map; 
+ A command: Corresponding to the scraping action to carry out; and 
+ one or more parameters for the selected command.

Currently, Dibbuk supports 12 scraping commands:

###getTag

###getTags

###getAttribute

###getAttributes

###getTagInContext

###getTagsInContext

###getAttributeInContext

###getAttributesInContext

###getTagInContexts

###getTagsInContexts

###getAttributeInContexts

###getAttributesInContexts


## Example of Dibbuk Configuration File

```json
{
    "version": 0.01,
    "extractors": [
        {
            "key": "bold",
            "command": "getTag",
            "tag": "b"
        },
        {
            "key": "inContext",
            "command": "getTagInContext",
            "tag": "b",
            "ctx": "li"
        },
        {
            "key": "inAllContext",
            "command": "getTagInContexts",
            "tag": "a",
            "ctx": "li"
        },
        {
            "key": "allInContext",
            "command": "getTagsInContext",
            "tag": "b",
            "ctx": "li"
        },
        {
            "key": "allInContexts",
            "command": "getTagsInContexts",
            "tag": "b",
            "ctx": "li"
        },
        {
            "key": "urls",
            "command": "getAttributes",
            "att": "href",
            "tag": "a"
        },
        {
            "key": "urlInContext",
            "command": "getAttributeInContext",
            "att": "href",
            "tag": "a",
            "ctx": "li"
        },
        {
            "key": "urlInContexts",
            "command": "getAttributeInContexts",
            "att": "href",
            "tag": "a",
            "ctx": "li"
        },
        {
            "key": "urlsInContext",
            "command": "getAttributesInContext",
            "att": "href",
            "tag": "a",
            "ctx": "li"
        },
        {
            "key": "urlsInContexts",
            "command": "getAttributesInContexts",
            "att": "href",
            "tag": "a",
            "ctx": "li"
        },
        {
            "key": "url",
            "command": "getAttribute",
            "att": "href",
            "tag": "a"
        },
        {
            "key": "items",
            "command": "getTags",
            "tag": "li"
        }
    ]
}
```