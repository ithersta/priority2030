require 'petrovich'

def genitive(lastname, firstname, middlename)
  Petrovich(
    lastname: lastname,
    firstname: firstname,
    middlename: middlename,
  ).genitive.to_s
end
