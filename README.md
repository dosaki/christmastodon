christmastodon
==============

<img src="https://raw.githubusercontent.com/dosaki/christmastodon/master/christmastodon.png"/>

###### mascot by [Zariga](https://github.com/zarigasofia)

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

Edit the message and subject files with what you want. `[recipient]` and `[match]` are variables that will be replaced.

Send it with
```
groovy Match.groovy
```

Or resend the already-matched list:
```
groovy Resend.groovy <email here> <backup match file here>
```
