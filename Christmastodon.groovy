@GrabConfig( systemClassLoader = true )
@Grab( group = 'javax.mail', module = 'mail', version = '1.4.7' )
@Grab( group = 'javax.activation', module = 'activation', version = '1.1.1' )

import javax.mail.*
import javax.activation.*
import javax.mail.internet.*

class Christmastodon{
  def message
  def subject
  def user
  def password

  Christmastodon(){
    this.message = new File('message').getText('UTF-8')
    this.subject = new File('subject').getText('UTF-8')

    def credentialsFile = new File('credentials')
    this.user = credentialsFile.readLines().get(0)
    this.password = credentialsFile.readLines().get(1)
  }

  def hasSelfGifting(list, rndList){
    for (int i = 0; i<list.size(); i++){
      if(list[i][0] == rndList[i][0]){
        return true
      }
    }
    return false
  }

  def findNameFromEmail(email, people){
    return people.find{
      it[1] == email
    }
  }

  def findEmailFromName(name, people){
    return people.find{
      it[0] == name
    }
  }

  def email(fromAddress, toAddress, subject, message){
    def port = 465
    def host = 'smtp.gmail.com'

    def props = new Properties()
    props.put("mail.smtp.user", this.user)
    props.put("mail.smtp.host", host)
    props.put("mail.smtp.port", port)
    props.put("mail.smtp.starttls.enable","true")
    props.put("mail.smtp.debug", "true");
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.socketFactory.port", port)
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
    props.put("mail.smtp.socketFactory.fallback", "false")

    //def auth = new SMTPAuthenticator()
    def session = Session.getDefaultInstance(props, null)

    // Construct the message
    def msg = new MimeMessage(session)
    def to = new InternetAddress(toAddress)
    msg.from = new InternetAddress(fromAddress)
    msg.sentDate = new Date()
    msg.subject = subject
    msg.setRecipient(Message.RecipientType.TO, to)
    msg.setContent(message,'text/plain; charset=UTF-8')

    Transport transport = session.getTransport("smtps");
    transport.connect (host, port, this.user, this.password);
    transport.sendMessage(msg, msg.getAllRecipients());
    transport.close();
  }

  def emailMatch(recipient, matched, isDebug=false){
    def subject = this.subject

    def recipientName = recipient[0]
    def recipientEmail = recipient[1]
    
    def matchName = matched[0]
    def matchAddress = matched.size() > 2 ? matched[2] : null//post address
    
    def fromEmail = this.user
    def message = this.message
        .replace("[recipient]", recipientName)
        .replace("[match]", matchName)
    if(matchAddress){
      message = message.replace("[match_address]", matchAddress)
    }

    println "Sending email to " + recipientEmail
    if(!isDebug){
      email(fromEmail, recipientEmail, subject, message.toString())
    }
  }

  def testMatch(recipient, matched){
    println "${recipient[0]} is giving to ${matched[0]}"
  }

  def resend(recipient, file, people){
    def recipientName = findNameFromEmail(recipient, people)

    new File( file ).eachLine { line ->
      if(!(line.startsWith('YOU REALLY SHOULDN\'T BE PEEKING INSIDE!') || line == "")){
        def secretPair = line.split(" giving to ")
        if(secretPair[0].trim() == recipientName[0].trim() && verify("Found match for ${recipient} -> ${secretPair[0]}. Is this right?", "Cancelling...")){
          //testMatch([recipient], [secretPair[1].trim()])
          emailMatch([recipientName[0], recipient], [secretPair[1].trim()])
        }
      }
    }
  }

  def reSort(file, people){
    def givers = people.clone()
    def receivers = []

    def pairs = [:]
    new File( file ).eachLine { line ->
      if(!(line.startsWith('YOU REALLY SHOULDN\'T BE PEEKING INSIDE!') || line == "")){
        def secretPair = line.split(" giving to ")
        pairs[secretPair[0].trim()] = secretPair[1].trim()
      }
    }


    givers.each{ giver ->
      def trimmedGiver = giver[0].trim()
      if (pairs.containsKey(trimmedGiver)){
        receivers.push([pairs[trimmedGiver], ""])
      }
      else {
        receivers.push([trimmedGiver, "unmatched"])
      }
    }

    matchLists(givers, receivers)
  }

  def match(people){
    matchLists(people, people.clone())
  }

  def matchLists(people, rndPeople, isDebug=false){
    Collections.shuffle(rndPeople)
    while (hasSelfGifting(people, rndPeople)){
      println "> Oops! Rematching..."
      Collections.shuffle(rndPeople)
    }

    if(isDebug){
      for (int i = 0; i<rndPeople.size(); i++){
        testMatch(people[i],rndPeople[i])
        emailMatch(people[i],rndPeople[i], true)
      }
    } else {
      new File(".backup_match").withWriter { out ->
        out.println "YOU REALLY SHOULDN'T BE PEEKING INSIDE!"
        for (int i = 0; i<rndPeople.size(); i++){
          out.println people[i][0] + " giving to " + rndPeople[i][0]
          emailMatch(people[i],rndPeople[i])
        }
      }
    }
  }

  def loadList(file){
    println "> Checking the 'Good' list."
    def people = []
    new File( file ).eachLine { line ->
      def splittedLine = line.split(" - ")
      if(!(line.startsWith('#') || line == ""))
        people << [
          splittedLine[0].trim(),
          splittedLine[1].trim(),
          splittedLine[2].trim()
        ]
    }

    return people
  }

  def verify(prompt, noMessage){
    def console = System.console()
    if (console) {
      def approved = console.readLine('> '+prompt+' [y/N] ')
      if(approved.toUpperCase() != "Y"){
        println "> " + noMessage
        return false
      }
      else{
        return true
      }
    }
    else {
      logger.error "> This is embarassing. I cannot get the console."
      return false
    }
  }
}
