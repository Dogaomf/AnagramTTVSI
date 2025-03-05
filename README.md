Para encontrar todas as possíveis permutações de uma palavra, seguimos um processo sistemático de troca de caracteres e chamadas recursivas.

Passo 1: Trocando caracteres
O algoritmo começa percorrendo cada posição da string e trocando a letra atual por todas as outras possíveis. Essa troca permite explorar todas as combinações possíveis.

Passo 2: Chamada recursiva
Após cada troca, o algoritmo chama a função recursivamente para processar os caracteres restantes. Esse passo garante que todas as combinações de letras sejam geradas corretamente.

Passo 3: Salvando o resultado
Quando o algoritmo chega ao final da string, significa que encontrou uma permutação completa. Neste momento, a combinação atual é adicionada à lista de resultados.

Passo 4: Revertendo a troca
Depois de registrar uma permutação, a troca realizada anteriormente é desfeita. Isso permite que o algoritmo tente outras combinações sem afetar as iterações seguintes.

Exemplo prático com "abc"
Vamos aplicar esse processo para gerar todas as permutações da palavra "abc".

Começamos com "abc" e trocamos a primeira letra com todas as outras:

"abc" → Mantemos "a" e permutamos os próximos caracteres.
"acb" → Trocamos "b" com "c".
Seguimos para a segunda posição e fazemos novas trocas:

"abc" → Como "b" já está no lugar, seguimos.
"acb" → Como "c" já está no lugar, seguimos.
Quando atingimos a última posição, registramos a palavra na lista de resultados.

Repetimos o processo para outras trocas na posição inicial:

"bac" e "bca" são geradas ao trocar "a" com "b".
"cab" e "cba" são geradas ao trocar "a" com "c".
Ao final do processo, temos todas as permutações possíveis:

Resultado final:
["abc", "acb", "bac", "bca", "cab", "cba"]

Esse método garante que todas as combinações possíveis de uma palavra sejam exploradas de forma organizada e eficiente.

Via Postman fiz a chamada GET abaixo:

http://localhost:8080/anagrams/frw

Me retornou um Json que vou compartilhar:

[
"frw",
"fwr",
"rfw",
"rwf",
"wrf",
"wfr"
]

Funciona para qualquer grupo de letras e valida se esta vazia ou se contem algum outro caracter que não seja letras, e retorna a mensagem :

"Input must be a non-empty string containing only letters."
