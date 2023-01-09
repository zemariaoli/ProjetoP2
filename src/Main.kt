import java.io.File

fun main(){

    val mensagemMenorDeIdade = "Menor de idade nao pode jogar"
    val mensagemDataInvalida = "Data invalida"
    var terminar:Boolean
    var idadeResultado:String?

    do {
        terminar = true

        if (menu() == 1) {
            terminar = false
            val numLinhas = linha()
            val numColunas = coluna()

            if (!validaTamanhoMapa(numLinhas , numColunas)){
                println("Terreno Invalido")
            }else {
                var continuar = true
                if (numLinhas == 10 && numColunas == 10) {
                    do {
                        val data = pedirData()
                        idadeResultado = (validaDataNascimento(data))
                        if (idadeResultado != null) println(idadeResultado)
                        if (idadeResultado == mensagemMenorDeIdade) continuar = false
                    } while (idadeResultado == mensagemDataInvalida)
                }
                if (continuar) {
                    print("\n")

                    val contadoresVerticais = leContadoresDoFicheiro(numLinhas,numColunas,true)
                    val contadoresHorizontais = leContadoresDoFicheiro(numLinhas,numColunas,false)
                    val terreno = leTerrenoDoFicheiro(numLinhas,numColunas)
                    println(criaTerreno(terreno, contadoresVerticais, contadoresHorizontais, true, true))

                    do {
                        continuar = false
                        println("Coordenadas da tenda? (ex: 1,B)")
                        val coordenadasStr = readLine()
                        if (coordenadasStr == "sair"){
                            terminar = true
                        }
                        else {
                            val resultadoCoordenadas = processaCoordenadas(coordenadasStr, numLinhas, numColunas)
                            if (resultadoCoordenadas == null) {
                                println("Coordenadas invalidas")
                            } else {

                                val linha = resultadoCoordenadas.first
                                val coluna = resultadoCoordenadas.second

                                if (colocaTenda(terreno, Pair(linha, coluna)) == false) {
                                    println("Tenda nao pode ser colocada nestas coordenadas")
                                }
                                else {
                                    println()
                                    println(criaTerreno(terreno, contadoresVerticais, contadoresHorizontais, true, true))
                                }

                                if (terminouJogo(terreno, contadoresVerticais, contadoresHorizontais) == true) {
                                    println("Parabens! Terminou o jogo!")
                                    continuar = true
                                }
                            }
                        }
                    } while (!terminar && !continuar)
                }
            }
        }
    } while (!terminar)
}

fun criaMenu(): String {

    return "\nBem vindo ao jogo das tendas\n\n1 - Novo jogo\n0 - Sair\n"

}

fun menu(): Int{

    var opcao:Int?
    do {
        println(criaMenu())
        opcao = readLine()?.toIntOrNull()

        if (opcao != 1 && opcao != 0) {
            println("Opcao invalida")
        }

    } while (opcao != 1 && opcao != 0)
    return opcao
}

fun linha():Int{

    val respostaInvalida = "Resposta invalida"
    var numLinhas:Int?
    do {
        println("Quantas linhas?")
        numLinhas = readLine()?.toIntOrNull()

        if (numLinhas == null || numLinhas <= 0){
            println(respostaInvalida)
        }

    }while ( numLinhas == null || numLinhas <= 0)
    return numLinhas
}

fun coluna():Int{

    val respostaInvalida = "Resposta invalida"
    var numColunas:Int?
    do {
        println("Quantas colunas?")
        numColunas = readLine()?.toIntOrNull()

        if (numColunas == null || numColunas <= 0) {
            println(respostaInvalida)
        }

    }while (numColunas == null || numColunas <= 0)
    return numColunas
}

fun validaTamanhoMapa(numLinhas: Int, numColunas: Int): Boolean{
    if (numLinhas == 5 && numColunas == 5) return true
    if (numLinhas == 6 && (numColunas == 5 || numColunas == 6)) return true
    if (numLinhas == 8 && (numColunas == 8 || numColunas == 10)) return true
    if (numLinhas == 10 && (numColunas == 8 || numColunas == 10)) return true
    return false
}

fun pedirData(): String {

    println("Qual a sua data de nascimento? (dd-mm-yyyy)")
    return readLine().toString()
}

fun validaDataNascimento(data: String?) : String? {

    val mensagemMenorDeIdade = "Menor de idade nao pode jogar"
    val mensagemDataInvalida = "Data invalida"

    if (data == null) {
        return mensagemDataInvalida
    }
    if (data.length != 10) {
        return mensagemDataInvalida
    }
    if (data[2] != '-' || data[5] != '-'){
        return mensagemDataInvalida
    }

    val dia = (data[0].toString() + data[1].toString()).toInt()
    val mes = (data[3].toString() + data[4].toString()).toInt()
    val ano = (data[6].toString() + data[7].toString() + data[8].toString() + data[9].toString()).toInt()

    if (dia !in 1..31){
        return mensagemDataInvalida
    }
    if(mes !in 1..12){
        return mensagemDataInvalida
    }
    if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia == 31) {
        return mensagemDataInvalida
    }
    if (mes == 2) {
        if ((ano % 4 == 0 && ano % 100 != 0) || ano % 400 == 0) {
            if (dia > 29)  return mensagemDataInvalida
        }
        else{
            if (dia > 28) return mensagemDataInvalida
        }

    }
    if (ano < 1900 || ano >= 2022) {
        return mensagemDataInvalida
    }
    if (ano > 2004) {
        return mensagemMenorDeIdade
    }
    if (ano == 2004 && mes >= 11) {
        return mensagemMenorDeIdade
    }
    return null
}

fun criaLegendaHorizontal(numColunas: Int): String{

    var contador = 1
    var cabecalho = ""
    var letra: Int

    while (contador <= numColunas){
        letra = 64 + contador
        cabecalho += letra.toChar()
        if (contador != numColunas) cabecalho += " | "
        contador++
    }

    return cabecalho
}

fun criaLegendaContadoresHorizontal(contadoresVerticais: Array<Int?>): String {
    var linha = ""

    for (count in contadoresVerticais.indices){

        if (contadoresVerticais[count] == null){
            linha += " "
        }
        else linha += contadoresVerticais[count]

        if (count < contadoresVerticais.size - 1){
            linha += "   "
        }

    }

    return linha
}

fun leTerrenoDoFicheiro(numLines: Int, numColumns: Int):Array<Array<String?>>{

    val terreno = Array(numLines) {arrayOfNulls<String?>(numColumns)}

    val ficheiro = File("$numLines" + "x" + "$numColumns" + ".txt").readLines()

    for (linha in 2 until ficheiro.size){
        val partes = ficheiro[linha].split(",")
        val lin = partes[0].toInt()
        val col = partes[1].toInt()

        terreno[lin][col] = "A"

    }

    return terreno

}

fun leContadoresDoFicheiro(numLines: Int, numColumns: Int, verticais: Boolean): Array<Int?>{
    val partes: List<String>
    val linhas = File("$numLines" + "x" + "$numColumns" + ".txt").readLines()

    if (verticais){
        partes = linhas[0].split(",")
    } else {
        partes = linhas[1].split(",")
    }

    val contadores = arrayOfNulls<Int?>(partes.size)

    for (i in partes.indices)
    {
        contadores[i] = partes[i].toIntOrNull()
        if (partes[i] == "0") contadores[i] = null
    }

    return contadores
}

fun criaTerreno(terreno: Array<Array<String?>>,
                contadoresVerticais: Array<Int?>?,
                contadoresHorizontais: Array<Int?>?,
                mostraLegendaHorizontal: Boolean,
                mostraLegendaVertical: Boolean): String {

    var tabuleiro = ""

    if (contadoresVerticais != null) {
        tabuleiro += "       "
        tabuleiro += criaLegendaContadoresHorizontal(contadoresVerticais) + "\n"
    }

    if (mostraLegendaHorizontal) {
        tabuleiro += "    "

        tabuleiro += " | "

        tabuleiro += criaLegendaHorizontal(terreno[0].size) + "\n"
    }

    var contalinhas = 1

    while (contalinhas <= terreno.size) {

        var contador: String? = "  "

        if (contadoresHorizontais != null) {

            contador = contadoresHorizontais[contalinhas - 1].toString()

            if (contador == "null") contador = " "

            contador += " "

        }

        tabuleiro += "$contador"

        if (mostraLegendaVertical) {

            if (contalinhas < 10) {

                tabuleiro += " "

            }

            tabuleiro += "$contalinhas"
        }else{
            tabuleiro += "  "
        }

        for (i in 0 until terreno[0].size){

            tabuleiro += " | "

            var casa = terreno[contalinhas - 1][i]
            if (casa == "A")  casa = "△"
            if (casa == null) casa = " "

            tabuleiro += casa
        }
        if (contalinhas != terreno.size) tabuleiro += "\n"

        contalinhas++
    }

    return tabuleiro
}

fun ascii(coluna : Char): Int {

    val codigoAscii: Int = coluna.code

    return codigoAscii - 64
}

fun processaCoordenadas(coordenadasStr: String?, numLines: Int, numColumns: Int): Pair<Int,Int>? {

    if (coordenadasStr == null) return null

    if (coordenadasStr.length < 3 || coordenadasStr.length > 4) return null

    if (coordenadasStr[coordenadasStr.length - 2] != ',') return null

    val partes = coordenadasStr.split(",")

    val linha = partes[0].toInt()

    val coluna = partes[1]

    val ascii = ascii(coluna[0])

    if (linha !in 1..numLines) return null

    if (ascii !in 1..numColumns) return null

    return Pair(linha - 1, ascii - 1)
}

fun temArvoreAdjacente(terreno: Array<Array<String?>>, coords: Pair<Int, Int>) : Boolean {

    val linha = coords.first
    val coluna = coords.second

    if (linha != 0) {
        if (terreno[linha - 1][coluna] == "A") return true
    }
    if (linha != terreno.size - 1){
        if (terreno[linha + 1][coluna] == "A") return true
    }

    if (coluna != 0) {
        if (terreno[linha][coluna - 1] == "A") return true
    }
    if (coluna != terreno[linha].size - 1){
        if (terreno[linha][coluna + 1] == "A") return true
    }

    return false
}

fun temTendaAdjacente(terreno: Array<Array<String?>>, coords: Pair<Int, Int>) : Boolean{

    val linha = coords.first
    val coluna = coords.second

    if (linha != 0) {
        if (terreno[linha - 1][coluna] == "T") return true
        if (coluna != 0) if (terreno[linha - 1][coluna - 1] == "T") return true
        if (coluna != terreno[linha].size - 1) if (terreno[linha - 1][coluna + 1] == "T") return true
    }
    if (linha != terreno.size - 1) {
        if (terreno[linha + 1][coluna] == "T") return true
        if (coluna != 0) if (terreno[linha + 1][coluna - 1] == "T") return true
        if (coluna != terreno[linha].size - 1) if (terreno[linha + 1][coluna + 1] == "T") return true
    }
    if (coluna != 0) {
        if (terreno[linha][coluna - 1] == "T") return true
    }
    if (coluna != terreno[linha].size - 1) {
        if (terreno[linha][coluna + 1] == "T") return true
    }

    return false
}

fun contaTendasColuna(terreno:Array<Array<String?>>, coluna: Int): Int{

    var tendas = 0

    if (coluna >= 0 && coluna < terreno[0].size) {

        for (linha in terreno.indices) {

            if (terreno[linha][coluna] == "T") tendas++

        }
    }

    return tendas
}

fun contaTendasLinha(terreno: Array<Array<String?>>, linha: Int): Int {

    var tendas = 0

    if (linha >= 0 && linha < terreno.size) {

        for (coluna in 0 until terreno[linha].size) {

            if (terreno[linha][coluna] == "T") tendas++

        }
    }

    return tendas
}

fun colocaTenda(terreno: Array<Array<String?>>, coords: Pair<Int, Int>): Boolean {

    val linha = coords.first
    val coluna = coords.second

    if (terreno[linha][coluna] == "A") return false

    if (temTendaAdjacente(terreno, coords)) return false

    if (!temArvoreAdjacente(terreno, coords)) return false

    if (terreno[linha][coluna] == "T") {
        terreno[linha][coluna] = null
    } else {
        terreno[linha][coluna] = "T"
    }

    return true
}

fun terminouJogo(terreno: Array<Array<String?>>, contadoresVerticais: Array<Int?>, contadoresHorizontais: Array<Int?>): Boolean{

    for (linha in terreno.indices) {

        var contador = contadoresHorizontais[linha]

        if (contador == null) contador = 0

        if (contaTendasLinha(terreno,linha) != contador) return false

    }

    for (coluna in 0 until terreno[0].size) {

        var contador = contadoresVerticais[coluna]

        if (contador == null) contador = 0

        if (contaTendasColuna(terreno, coluna) != contador) return false

    }

    return true
}
