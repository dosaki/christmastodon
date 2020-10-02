import Christmastodon


def christmastodon = new Christmastodon();

def people = christmastodon.loadList('list')

println "> I found ${people.size()} people:"
people.each {
  println "  - " + it[0] + " (" + it[1] + ")"
}

if(!christmastodon.verify("Can I send the emails?", "No?! Fine... ruin Christmas for everyone.")){
  return false
}

println "> Matching and sending..."
christmastodon.match(people);
println "> Done!"
