@GrabConfig( systemClassLoader = true )
@Grab( group = 'javax.mail', module = 'mail', version = '1.4.7' )
@Grab( group = 'javax.activation', module = 'activation', version = '1.1.1' )

import javax.mail.*
import javax.activation.*
import javax.mail.internet.*

this.credentialsFile= new File('credentials')
this.user = credentialsFile.readLines().get(0)
this.password = credentialsFile.readLines().get(1)


def hasSelfGifting(list, rndList){
    for (int i = 0; i<list.size(); i++){
        if(list[i] == rndList[i]){
            return true
        }
    }
    return false
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
    session = Session.getDefaultInstance(props, null)

    // Construct the message
    msg = new MimeMessage(session)
    to = new InternetAddress(toAddress)
    msg.from = new InternetAddress(fromAddress)
    msg.sentDate = new Date()
    msg.subject = subject
    msg.setRecipient(Message.RecipientType.TO, to)
    msg.setContent(message,'text/plain')

    Transport transport = session.getTransport("smtps");
    transport.connect (host, port, this.user, this.password);
    transport.sendMessage(msg, msg.getAllRecipients());
    transport.close();
}

def emailMatch(recipient, matched){
    def subject = "Secret Santa para o Jantar de Natal - dia 13 de Dezembro"
    def toAddress = recipient[1]
    def fromAddress = this.user
    def message = """
Boas ${recipient[0]},

Para o Jantar de Natal do dia 13 de Dezembro, tu és o Secret Santa de ${matched[0]}.

Lembra-te que só podes gastar até 5 euros!

Cumprimentos,
Tiago Correia

PS.: Eu não sei quem é o secret santa de quem... fiz um scriptzinho para randomizar a malta e enviar os mails.
    """
    println "Sending to " + toAddress
    email(fromAddress, toAddress, subject, message.toString())
}

def match(people){
    def rndPeople = people.clone()

    Collections.shuffle(rndPeople)
    while (hasSelfGifting(people, rndPeople)){
        println "> Oops! Rematching..."
        Collections.shuffle(rndPeople)
    }

    new File(".backup_match").withWriter { out ->
        out.println "YOU REALLY SHOULDN'T BE PEEKING INSIDE!"
        for (int i = 0; i<rndPeople.size(); i++){
            out.println people[i][0] + " giving to " + rndPeople[i][0]
            emailMatch(people[i],rndPeople[i])
        }
    }
}

println "> Checking the 'Good' list."
def people = []
new File( 'list' ).eachLine { line ->
    if(!(line.startsWith('#') || line == ""))
        people << [
            line.split(" - ")[0].trim(),
            line.split(" - ")[1].trim()
        ]
}

println "> Hmm... I found these people:"
people.each{
    println "  - " + it[0] + " (" + it[1] + ")"
}

def console = System.console()
if (console) {
    approved = console.readLine('> Mind if I send the emails? [y/N] ')
    if(approved.toUpperCase() != "Y"){
        println "> No?! Fine... ruin Christmas for everyone."
        return;
    }
}
else {
    logger.error "> This is embarassing. I cannot get the console."
}

println "> Matching and sending..."
match(people);
println "> Done!"
