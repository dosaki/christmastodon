import Christmastodon

def christmastodon = new Christmastodon();

if(!christmastodon.verify("Using backup file: " + args[1], "Continue?")){
  return false
}
if(!christmastodon.verify("Can I send the email to " + args[0] + "?", "No?! Ok... Well, thanks for popping by.")){
  return false
}

println "> All right, resending..."
christmastodon.resend(args[0], args[1], christmastodon.loadList('list'))
println "> Done!"
