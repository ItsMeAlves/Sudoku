/*
 *  Scala Sudoku Solver
 *  Implementação com Scala, usando algoritmo de backtracking
 *  Aluno: Gabriel Alves de Lima (http://github.com/ItsMeAlves/Sudoku)
 *  Para execução, entrar na pasta que contém o arquivo csudoku.scala
 *  Executar 'scalac csudoku.scala'
 *			 'scala main' 
 *  O código irá pedir o tabuleiro de cima para baixo
 */

//Cria a classe Tabuleiro, que vai representar uma matriz do sudoku 9x9
//O armazenamento será feito por uma string
class Table(val content:String = "") {

	//Método que verifica espaços vazios no conteúdo do tabuleiro
	//Retorna true caso o tabuleiro esteja completamente preenchido
	def isFull():Boolean = {
		!this.content.contains('0')
	}

	//Verifica se um tabuleiro é válido, ou seja, obedece as regras do sudoku
	//Essas regras são:
	//	- Uma linha não pode ter um elemento repetido
	//	- Uma coluna não pode ter um elemento repetido
	//	- Um quadrante não pode ter um elemento repetido
	//Esse método faz essas três verificações
	//Retorna true caso o tabuleiro seja válido
	def isValid():Boolean = {
		//Função auxiliar, que verifica, por recursão, se numa string existe elementos repetidos
		//A função ignora zeros
		def verifyMultipleElements(s:String):Boolean = {
			//Usando listas, pode-se fazer uso do casamento de padrões
			//Por isso, transformamos a string recebida em uma lista de chars
			val list = s.toList
			list match {
				case Nil => false
				case List(v) => false
				case h :: t => {
					if(t.contains(h) && h != '0') {
						true
					} 
					else {
						verifyMultipleElements(t.mkString)
					}
				}
			}
		}

		//Faz a verificação de pertinência múltipla em cada linha do tabuleiro
		//Retorna true se está tudo conforme as regras
		def verifyRows():Boolean = {
			var answer:Boolean = true
			for(a <- 0 to 8) {
				val row = this.content.substring(a * 9, (a * 9) + 9)
				answer = answer && !verifyMultipleElements(row)
			}
			answer
		}

		//Faz a verificação de pertinência múltipla em cada coluna do tabuleiro
		//Retorna true se está tudo conforme as regras
		def verifyColumns():Boolean = {
			var answer:Boolean = true
			for(a <- 0 to 8) {
				val column = "" + this.content(a) + this.content(a + 9) +
					this.content(a + 18) + this.content(a + 27) +
					this.content(a + 36) + this.content(a + 45) +
					this.content(a + 54) + this.content(a + 63) +
					this.content(a + 72)
				answer = answer && !verifyMultipleElements(column)
			}
			answer
		}

		//Faz a verificação de pertinência múltipla em cada quadrante do tabuleiro
		//Retorna true se está tudo conforme as regras
		def verifySquares():Boolean = {
			var answer:Boolean = true
			for(a <- 0 to 2) {
				val squares = "" + this.content(a*3) + this.content(a*3 + 1) +
					this.content(a*3 + 2) + this.content(a*3 + 9) +
					this.content(a*3 + 10) + this.content(a*3 + 11) +
					this.content(a*3 + 18) + this.content(a*3 + 19) +
					this.content(a*3 + 20)
				answer = answer && !verifyMultipleElements(squares)
			}
			for(a <- 0 to 2) {
				val squares = "" + this.content(a*3 + 27) + this.content(a*3 + 28) +
					this.content(a*3 + 29) + this.content(a*3 + 36) +
					this.content(a*3 + 37) + this.content(a*3 + 38) +
					this.content(a*3 + 45) + this.content(a*3 + 46) +
					this.content(a*3 + 47)
				answer = answer && !verifyMultipleElements(squares)
			}
			for(a <- 0 to 2) {
				val squares = "" + this.content(a*3 + 54) + this.content(a*3 + 55) +
					this.content(a*3 + 56) + this.content(a*3 + 63) +
					this.content(a*3 + 64) + this.content(a*3 + 65) +
					this.content(a*3 + 72) + this.content(a*3 + 73) +
					this.content(a*3 + 74)
				answer = answer && !verifyMultipleElements(squares)
			}
			answer	
		}

		//Só é válido se todas as regras forem válidas
		val rowsRule:Boolean = verifyRows()
		val columnsRule:Boolean = verifyColumns()
		val squaresRule:Boolean = verifySquares()

		rowsRule && columnsRule && squaresRule
	}

	//Lista as próximas jogadas válidas
	//Caso o tabuleiro já esteja preenchido, não há novas jogadas
	def listNextMoves():List[Table] = {
		if(this.isFull()) {
			List()
		}
		else {
			var answers:List[Table] = List()
			//Gera tabuleiros com todos os valores possíveis como próxima jogada
			//Porém, só serão respostas os tabuleiros válidos
			for(value <- 1 to 9) {
				val newContent = this.content.replaceFirst("0", "" + value)
				val table = new Table(newContent)
				if(table.isValid()) {
					answers = answers :+ table
				}
			}
			answers
		}
	}

	//Retorna o tabuleiro em string, porém formatado em uma matriz 9x9
	def mkString():String = {
		var str:String = ""
		for(a <- 0 to 8) {
			str = str + this.content.substring(a*9, a*9 + 9) + "\n"
		}
		str
	}
} 

object main extends App {
	//Imprime as instruções
	println("\n\n\n\n")
	println("********************* Scala Sudoku solver ***********************")
	println("")
	println("Digite cada linha que deve entrar no tabuleiro")
	println("Os espaços vazios devem ser zeros")
	println("Caso a linha dada tenha menos de 9 valores, ela será completada com zeros")
	println("")
	println("*****************************************************************")

	//Obtém as linhas do tabuleiro
	//Os zeros são espaços vazios	
	//Os zeros preenchem as linhas menores que 9
	def prompt(amount:Int):String = {
		def aux(count:Int,s:String):String = {
			if(count > 0) {
				val promptLine:String = scala.io.StdIn.readLine("promptLine " +
					"(faltam " + count + ")>")
				val line = promptLine.padTo(amount, "0").mkString
				aux(count - 1, s + line)
			}
			else {
				s
			}
		}	
		aux(amount, "")
	}

	//Função que vai resolver o tabuleiro
	//Recebe um tabuleiro
	//Caso este tabuleiro esteja preenchido, imprime este tabuleiro
	//Caso contrário, resolve todas as próximas jogadas válidas do tabuleiro inicial
	//Esta é a função que representa o algoritmo de backtracking
	def solve(t:Table):Unit = {
		if(t.isFull()) {
			println(t.mkString)
		}
		else {
			t.listNextMoves().foreach((x) => solve(x))
		}
	}

	//Cria o tabuleiro inicial
	val initialContent:String = prompt(9)
	val initialTable:Table = new Table(initialContent)

	println("\n******************************************\n")
	println("Resultados possíveis")
	println("\n******************************************\n")
	
	//Resolve o tabuleiro inicial
	solve(initialTable)
	println("Fim - Gabriel Alves de Lima")
}