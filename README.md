# tools

Some tools I wrote that may be useful to others.

## JsonConverter
This java tool can convert Talentd API Tester exports to Insomnia format. It's a quick and dirty tool and it worked for me. It's just reading the provided export file and outputs the convertion to stdout. It converts Talentd projects to Insomnia collections and should keep the order. It assumes all requests are JSON. It does not account for environments.

JsonConverter.java 
