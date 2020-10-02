import Christmastodon

def christmastodon = new Christmastodon();


if(!christmastodon.verify("Using backup file: " + args[1], "Exiting...")){
  return false
}

if(["resort", "rematch"].contains(args[0])){
  if(!christmastodon.verify("Re-sort the targets of the following users:\n${christmastodon.loadList('list').join('\n')}\nProceeed?", "Exiting...")){
    return false
  }

  println "> All right, resorting..."
  christmastodon.reSort(args[1], christmastodon.loadList('list'))
  println "> Done!"
}
else {
  if(!christmastodon.verify("Can I send the email to " + args[0] + "?", "No?! Ok... Well, thanks for popping by.")){
    return false
  }

  println "> All right, resending..."
  christmastodon.resend(args[0], args[1], christmastodon.loadList('list'))
  println "> Done!"
}
