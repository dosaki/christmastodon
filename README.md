christmastodon
==============

<img src="https://raw.githubusercontent.com/dosaki/christmastodon/master/christmastodon.png"/>

###### mascot by [Zariga](https://github.com/zarigasofia)

Automatic Secret Santa! :D

## Dependencies

This requires [Groovy](http://groovy.codehaus.org/)!


## How to use it

Clone it `git clone https://github.com/dosaki/christmastodon.git`

Rename [`list.template`](https://github.com/dosaki/christmastodon/blob/master/list.template) to `list` and edit it with your names, emails and optionally an address.
Syntax is:
```
Person Name - the.email@example.com - Address
```

Rename the [`credentials.template`](https://github.com/dosaki/christmastodon/blob/master/credentials.template) to `credentials` and edit it with your gmail email and password. Syntax is:
```
your.email@gmail.com
yourpasswordhere
```

Rename the [`message.template`](https://github.com/dosaki/christmastodon/blob/master/message.template) to `message` and [`subject.template`](https://github.com/dosaki/christmastodon/blob/master/subject.template) to `subject` and edit them with what you want.
You can use the following placeholders:
* `[recipient]` - the name of the recipient of this email
* `[match]` - the name of their match
* `[match_address]` - the address of their match

Send it with
```
groovy Match.groovy
```

Or resend the already-matched list:
```
groovy Resend.groovy <email here> <backup match file here>
```
