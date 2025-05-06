fun main() {
    // Imprime a mensagem solicitando o nome do primeiro treinador
    println("Digite o nome do Treinador 1: ")

    // Lê o nome digitado e armazena na variável nomePlayer1
    // Se for nulo, usa string vazia como padrão
    val nomePlayer1 = readln() ?: ""

    // Imprime a mensagem solicitando o nome do segundo treinador
    println("Digite o nome do Treinador 2: ")

    // Lê o nome do segundo treinador usando readLine e armazena na variável nomePlayer2
    val nomePlayer2 = readLine() ?: ""

    // Exibe os nomes dos treinadores na tela
    println("A batalha será entre:")
    println("Treinador 1: $nomePlayer1")
    println("Treinador 2: $nomePlayer2")

    // Pausa o programa e espera o usuário pressionar ENTER
    println("Pressione a tecla ENTER para prosseguir com a batalha ")
    readLine()

    // Cria três listas para guardar os nomes dos Pokémon divididos por nível
    val pokemonsNivel1a19 = mutableListOf<String>()      // Liga fraca
    val pokemonsNivel20a49 = mutableListOf<String>()     // Liga média
    val pokemonsNivel50a100 = mutableListOf<String>()    // Liga forte

    // Percorre o mapa de todos os Pokémon (chave = nome, valor = atributos) e classifica os Pokémon
    mapaPokemons.forEach { chave, valor ->
        // Tenta pegar o nível do Pokémon e converter para Int
        val nivel = valor["NIVEL"]?.toString()?.toIntOrNull()

        // Adiciona o Pokémon na lista correspondente ao nível
        when {
            nivel in 1..19 -> pokemonsNivel1a19.add(chave)
            nivel in 20..49 -> pokemonsNivel20a49.add(chave)
            nivel in 50..100 -> pokemonsNivel50a100.add(chave)
        }
    }

    // Mostra as opções de nível para o jogador escolher a liga da batalha
    println("Escolha o nível para a batalha:")
    println("1 - Nível 1 (1 ao 19)")
    println("2 - Nível 2 (20 ao 49)")
    println("3 - Nível 3 (50 ao 100)")

    // Lê a escolha do nível e tenta converter para inteiro
    val nivelEscolhido = readLine()?.toIntOrNull()

    // Define quais Pokémon estarão disponíveis para a escolha com base na liga
    val pokemonsDisponiveis = when (nivelEscolhido) {
        1 -> pokemonsNivel1a19
        2 -> pokemonsNivel20a49
        3 -> pokemonsNivel50a100
        else -> {
            // Se o número não for 1, 2 ou 3, encerra o programa
            println("Opção inválida! Encerrando o programa.")
            return
        }
    }

    // Mostra os Pokémon disponíveis para aquela liga escolhida
    println("\nPokémons disponíveis para a Liga ${nivelEscolhido}:")
    if (pokemonsDisponiveis.isEmpty()) {
        println("Nenhum Pokémon disponível nessa liga.")
    } else {
        // Exibe todos os nomes de Pokémon disponíveis
        pokemonsDisponiveis.forEach { println(it) }
    }

    // Cria listas para armazenar os times de cada treinador
    val timePlayer1 = mutableListOf<String>()
    val timePlayer2 = mutableListOf<String>()

    // Função que exibe os Pokémon com numeração para facilitar a escolha
    fun mostrarPokemonsDisponiveis(pokemons: List<String>) {
        println("\nPokémons disponíveis:")
        pokemons.forEachIndexed { index, pokemon ->
            print("${index + 1} - $pokemon\t")  // Mostra o número + nome do Pokémon
            if ((index + 1) % 5 == 0) println() // Quebra linha a cada 5 Pokémon
        }
        println() // Pula linha no final
    }

    // Função que permite o jogador escolher um Pokémon
    fun escolherPokemon(timePlayer: MutableList<String>, nomePlayer: String) {
        mostrarPokemonsDisponiveis(pokemonsDisponiveis) // Mostra lista com numeração
        println("$nomePlayer, escolha seu Pokémon (digite o número):")
        val escolha = readLine()?.toIntOrNull() // Lê o número escolhido

        if (escolha != null && escolha in 1..pokemonsDisponiveis.size) {
            val pokemonEscolhido = pokemonsDisponiveis[escolha - 1] // Obtém Pokémon pela posição
            timePlayer.add(pokemonEscolhido) // Adiciona ao time
            pokemonsDisponiveis.removeAt(escolha - 1) // Remove da lista de disponíveis
            println("$nomePlayer escolheu: $pokemonEscolhido")
        } else {
            println("Escolha inválida. Tente novamente.")
            escolherPokemon(timePlayer, nomePlayer) // Tenta novamente se inválido
        }
    }

    // Cada jogador escolhe 3 Pokémon, alternadamente
    for (i in 1..3) {
        escolherPokemon(timePlayer1, nomePlayer1)
        escolherPokemon(timePlayer2, nomePlayer2)
    }

    // Exibe os times formados de cada treinador
    println("\nTimes finais:")
    println("$nomePlayer1: ${timePlayer1.joinToString(", ")}")
    println("$nomePlayer2: ${timePlayer2.joinToString(", ")}")

    // Espera ENTER antes de iniciar os confrontos
    println("\nTimes definidos! Aperte ENTER para gerar os confrontos")
    readLine()

    // Embaralha os Pokémon de cada jogador para os confrontos serem aleatórios
    val time1Embaralhado = timePlayer1.shuffled()
    val time2Embaralhado = timePlayer2.shuffled()

    // Função para simular um confronto entre dois Pokémon
    fun simularConfronto(poke1: String, poke2: String, mapa: Map<String, Map<String, Any>>): String {
        val p1 = mapa[poke1.lowercase()] ?: return "$poke1 não encontrado"
        val p2 = mapa[poke2.lowercase()] ?: return "$poke2 não encontrado"

        // Coleta os atributos dos dois Pokémon
        val nivel1 = p1["NIVEL"] as Int
        val ataque1 = p1["ATAQUE"] as Int
        val defesa1 = p1["DEFESA"] as Int
        val tipos1 = p1.keys.filter { it != "NIVEL" && it != "ATAQUE" && it != "DEFESA" }
        val vantagens1 = tipos1.flatMap { tipo -> (p1[tipo] as List<String>).flatMap { it.split(", ") } }

        val nivel2 = p2["NIVEL"] as Int
        val ataque2 = p2["ATAQUE"] as Int
        val defesa2 = p2["DEFESA"] as Int
        val tipos2 = p2.keys.filter { it != "NIVEL" && it != "ATAQUE" && it != "DEFESA" }
        val vantagens2 = tipos2.flatMap { tipo -> (p2[tipo] as List<String>).flatMap { it.split(", ") } }

        // Verifica se cada um tem vantagem de tipo sobre o outro
        val poke1TemVantagem = tipos2.any { tipo -> vantagens1.contains(tipo) }
        val poke2TemVantagem = tipos1.any { tipo -> vantagens2.contains(tipo) }

        // Define multiplicadores de vantagem
        val status1 = if (poke1TemVantagem) 1.5 else 1.0
        val status2 = if (poke2TemVantagem) 1.5 else 1.0

        // Calcula o poder de combate de cada Pokémon
        val poder1 = ((nivel1 + ataque1) * status1 + defesa1).toInt()
        val poder2 = ((nivel2 + ataque2) * status2 + defesa2).toInt()

        // Exibe os cálculos de poder
        println(">> $poke1: (($nivel1 + $ataque1) * $status1) + $defesa1 = $poder1")
        println(">> $poke2: (($nivel2 + $ataque2) * $status2) + $defesa2 = $poder2")

        // Retorna o resultado do confronto
        return when {
            poder1 > poder2 -> "$poke1 vence"
            poder2 > poder1 -> "$poke2 vence"
            else -> "Empate"
        }
    }

    // Função que mostra os 3 confrontos entre os times
    fun gerarConfrontos(
        timePlayer1: List<String>,
        timePlayer2: List<String>,
        nome1: String,
        nome2: String,
        mapa: Map<String, Map<String, Any>>
    ) {
        println("\nConfrontos:")
        for (i in 0 until 3) {
            val poke1 = timePlayer1[i]
            val poke2 = timePlayer2[i]
            println("Rodada ${i + 1}:")
            println("$nome1: $poke1 vs $nome2: $poke2")
            val resultado = simularConfronto(poke1, poke2, mapa)
            println(resultado)
        }
    }

    // Executa os confrontos usando os times embaralhados
    gerarConfrontos(time1Embaralhado, time2Embaralhado, nomePlayer1, nomePlayer2, mapaPokemons)

    // Inicia as contagens de vitória
    var vitoriasPlayer1 = 0
    var vitoriasPlayer2 = 0

    // Faz novamente os confrontos para contar pontos
    println("\nConfrontos:")
    for (i in 0 until 3) {
        val poke1 = timePlayer1[i]
        val poke2 = timePlayer2[i]
        println("Rodada ${i + 1}:")
        println("$nomePlayer1: $poke1 vs $nomePlayer2: $poke2")
        val resultado = simularConfronto(poke1, poke2, mapaPokemons)
        println(resultado)

        // Atualiza a pontuação com base no vencedor
        when {
            resultado.contains("$poke1 vence") -> vitoriasPlayer1++
            resultado.contains("$poke2 vence") -> vitoriasPlayer2++
        }
    }

    // Exibe o resultado final do torneio
    println("\nResultado final da MD3:")
    when {
        vitoriasPlayer1 > vitoriasPlayer2 -> println("$nomePlayer1 é o vencedor!")
        vitoriasPlayer2 > vitoriasPlayer1 -> println("$nomePlayer2 é o vencedor!")
        else -> println("Empate na MD3!")
    }
}
