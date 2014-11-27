christmastodon
==============

<img src="https://raw.githubusercontent.com/dosaki/christmastodon/master/christmastodon.png"/>

Automatic Secret Santa! :D

##Dependencies

This requires [Groovy](http://groovy.codehaus.org/)! Because I like it :P


##How to use it

Clone it `git clone https://github.com/dosaki/christmastodon.git`

Edit [`list`](https://github.com/dosaki/christmastodon/blob/master/list) with your names and emails.
Syntax is:
```
Person Name - the.email@example.com
```

Edit the [`credentials`](https://github.com/dosaki/christmastodon/blob/master/credentials) with your gmail email and password. Syntax is:
```
your.email@gmail.com
yourpasswordhere
```

You also need edit the message in [`emailMatch()` on `SecretSanta.groovy`](https://github.com/dosaki/christmastodon/blob/master/SecretSanta.groovy#L56).
I haven't separated that out yet. Sorry!
