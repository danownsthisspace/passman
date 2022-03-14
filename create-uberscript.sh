 #!/user/bin/bash
 
 bb uberscript src/passman/app -m passman.app

mkdir -p out

awk 'BEGIN{print "#!/usr/bin/env bb\n"}{print}' src/passman/app > out/passman

chmod +x out/passman

rm src/passman/app

 


