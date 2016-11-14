# <dibbuk/>
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

Currently, Dibbuk supports 12 extractors:

###getTag

Gets the text content of the first occurrence of a tag.
No entry is added to the result Map if the tag cannot be found in the scraped page.

Parameter: 
+ tag: The tag to scrape

####Example

```json
{
    "key": "bold",
    "command": "getTag",
    "tag": "b"
}
```

Get the text enclosed between the first occurrence of the <b></b> tags and returns it under key "bold".

`<b>one</b> and <b>two</b>` returns `Map("bold" -> "one")`

###getTags

Gets the text content of all the occurrences of a tag.
Returns an empty list (`List()`) if no tag is found in the scraped page.

Parameter: 
+ tag: The tag to scrape

####Example

```json
{
    "key": "bolds",
    "command": "getTags",
    "tag": "b"
}
```

Get the text enclosed between all the occurrences of the <b></b> tags and returns them under key "bolds".

`<b>one</b> and <b>two</b>` returns `Map("bolds" -> List("one","two"))`

###getAttribute

Gets the text content of the first occurrence of an attribute.
No entry is added to the result Map if the attribute cannot be found in the scraped page.

Parameters: 
+ att: The attribute to scrape
+ tag: The tag containing the attribute to scrape

####Example

```json
{
    "key": "url",
    "command": "getAttribute",
    "att": "href",
    "tag": "a"
}
```

Gets the text value of the first occurrence of <a href=""> tag and returns it under key "url".

`<a href="http://one.com">one</a> and <a href="http://two.com">two</a>` returns `Map("url" -> "http://one.com")`

###getAttributes

Gets the text content of all the occurrences of an attribute.
Returns an empty list (`List()`) if no attribute is found in the scraped page.

Parameters: 
+ att: The attribute to scrape
+ tag: The tag containing the attribute to scrape

####Example

```json
{
    "key": "urls",
    "command": "getAttributes",
    "att": "href",
    "tag": "a"
}
```

Get the text value of all the occurrences of `<a href="...">` tag and returns it under key "urls".

`<a href="http://one.com">one</a> and <a href="http://two.com">two</a>` returns `Map("urls" -> List("http://one.com","http://two.com"))`

###getTagInContext

Gets the text content of the first occurrence of a tag in the scope of the first occurrence of a context tag.
No entry is added to the result Map if the tag cannot be found in the context.

Parameter: 
+ tag: The tag to scrape
+ ctx: The context tag

####Example

```json
{
    "key": "inContext",
    "command": "getTagInContext",
    "tag": "b",
	"ctx": "li"
}
```

Get the text enclosed between the first occurrence of the <b></b> tags when it is in the context of the first occurrence of the `<li></li>` tags and returns it under key "inContext".

```html
<b>one</b>
<ul>
  <li><b>two</b>, <b>three</b>
  <li><b>four</b>, <b>five</b>
</ul>
<b>six</b>
```
returns `Map("inContext" -> "two"))`

###getTagsInContext

Gets the text content of all the occurrences of a tag in the scope of the first occurrence of a context tag.
An empty list is added to the result Map if the context cannot be found of if the tag cannot be found in the target context.

Parameter: 
+ tag: The tag to scrape
+ ctx: The context tag

####Example

```json
{
    "key": "allInContext",
    "command": "getTagsInContext",
    "tag": "b",
    "ctx": "li"
}
```

Get the text enclosed between all the occurrences of the <b></b> tags when it is in the context of the first occurrence of the `<li></li>` tags and returns it under key "allInContext".

```html
<b>one</b>
<ul>
  <li><b>two</b>, <b>three</b>
  <li><b>four</b>, <b>five</b>
</ul>
<b>six</b>
```
returns `Map("allInContext" -> List("two", "three"))`

###getTagInContexts

Gets the text content of the first occurrence of a tag in the scope of each of the occurrences of a context tag.
An empty list is added to the result Map if the context cannot be found of if the tag cannot be found in any of the target contexts.

Parameter: 
+ tag: The tag to scrape
+ ctx: The context tag

####Example

```json
{
    "key": "inAllContexts",
    "command": "getTagInContexts",
    "tag": "b",
    "ctx": "li"
}
```

Get the text enclosed between the first occurrence of the <b></b> tags when it is in the context of each of the occurrences of the `<li></li>` tags and returns it under key "inAllContexts".

```html
<b>one</b>
<ul>
  <li><b>two</b>, <b>three</b>
  <li><b>four</b>, <b>five</b>
</ul>
<b>six</b>
```
returns `Map("inAllContexts" -> List("two", "four"))`

###getTagsInContexts

Gets the text content of all the occurrences of a tag in the scope of each of the occurrences of a context tag.
An empty list is added to the result Map if the context cannot be found of if the tag cannot be found in any of the target contexts.

Parameter: 
+ tag: The tag to scrape
+ ctx: The context tag

####Example

```json
{
    "key": "allInContexts",
    "command": "getTagsInContexts",
    "tag": "b",
    "ctx": "li"
}
```

Get the text enclosed between all the occurrences of the <b></b> tags when it is in the context of any occurrence of the `<li></li>` tags and returns it under key "allInContexts".

```html
<b>one</b>
<ul>
  <li><b>two</b>, <b>three</b>
  <li><b>four</b>, <b>five</b>
</ul>
<b>six</b>
```
returns `Map("allInContexts" -> List("two", "three", "four", "five"))`

###getAttributeInContext

Gets the text content of the first occurrence of an attribute in the scope of the first occurrence of a context tag.
No entry is added to the result Map if the attribute cannot be found in the context.

Parameters: 
+ att: The attribute to scrape
+ tag: The tag containing the attribute to scrape
+ ctx: The context tag

####Example

```json
{
    "key": "urlInContext",
    "command": "getAttributeInContext",
    "att": "href",
    "tag": "a",
    "ctx": "li"
}
```

Gets the text value of the first occurrence of <a href=""> tag when it is in the context of the first occurrence of the `<li></li>` tags and returns it under key "urlInContext".

```html
<a href="one.html">one</a>
<ul>
  <li><a href="two.html">two</a>, <a href="three.html">three</a>
  <li><a href="four.html">four</a>, <a href="five.html">five</a>
</ul>
<a href="six.html">six</a>
```
returns `Map("urlInContext" -> "two.html")`

###getAttributesInContext

Gets the text content of all the occurrences of an attribute in the scope of the first occurrence of a context tag.
An empty list is added to the result Map if the context cannot be found of if the tag cannot be found in the context.

Parameters: 
+ att: The attribute to scrape
+ tag: The tag containing the attribute to scrape
+ ctx: The context tag

####Example

```json
{
    "key": "urlsInContext",
    "command": "getAttributesInContext",
    "att": "href",
    "tag": "a",
    "ctx": "li"
}
```

Gets the text value of all the occurrences of the <a href=""> tag when they are in the context of the first occurrence of the `<li></li>` tags and returns it under key "urlsInContext".

```html
<a href="one.html">one</a>
<ul>
  <li><a href="two.html">two</a>, <a href="three.html">three</a>
  <li><a href="four.html">four</a>, <a href="five.html">five</a>
</ul>
<a href="six.html">six</a>
```
returns `Map("urlInContext" -> List("two.html", "three.html"))`

###getAttributeInContexts

Gets the text content of the first occurrence of an attribute in each of the occurrences of a context tag.
An empty list is added to the result Map if the context cannot be found of if the tag cannot be found in the context.

Parameters: 
+ att: The attribute to scrape
+ tag: The tag containing the attribute to scrape
+ ctx: The context tag

####Example

```json
{
    "key": "urlInContexts",
    "command": "getAttributeInContexts",
    "att": "href",
    "tag": "a",
    "ctx": "li"
}
```

Gets the text value of the first occurrence of the <a href=""> tag in each of the occurrences of the `<li></li>` tags and returns it under key "urlInContexts".

```html
<a href="one.html">one</a>
<ul>
  <li><a href="two.html">two</a>, <a href="three.html">three</a>
  <li><a href="four.html">four</a>, <a href="five.html">five</a>
</ul>
<a href="six.html">six</a>
```
returns `Map("urlInContext" -> List("two.html", "four.html"))`

###getAttributesInContexts

Gets the text content of all the occurrences of an attribute in the scope of all the occurrences of a context tag.
An empty list is added to the result Map if the context cannot be found of if the tag cannot be found in any of the contexts.

Parameters: 
+ att: The attribute to scrape
+ tag: The tag containing the attribute to scrape
+ ctx: The context tag

####Example

```json
{
    "key": "urlsInContexts",
    "command": "getAttributesInContexts",
    "att": "href",
    "tag": "a",
    "ctx": "li"
}
```

Gets the text value of all the occurrences of the <a href=""> attribute when they are in the context of any of the occurrences of the `<li></li>` tags and returns it under key "urlsInContexts".

```html
<a href="one.html">one</a>
<ul>
  <li><a href="two.html">two</a>, <a href="three.html">three</a>
  <li><a href="four.html">four</a>, <a href="five.html">five</a>
</ul>
<a href="six.html">six</a>
```
returns `Map("urlInContext" -> List("two.html", "three.html", "four.html", "five.html"))`

## Example of a complete Dibbuk Configuration File

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
            "key": "inAllContexts",
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

##TODO List (in no particular order)
1. Add the possibility for command to operate within a given scope.
2. Add the possibility to postprocess extractor results
3. Add the possibility to further filter extractor results