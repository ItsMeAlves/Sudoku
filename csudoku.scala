/*
 *  Scala Sudoku Solver
 *  Implementação com Scala, usando algoritmo de backtracking
 *  Aluno: Gabriel Alves de Lima (http://github.com/ItsMeAlves/Sudoku)
 *  Para execução, entrar na pasta que contém o arquivo csudoku.scala
 *  Executar 'scalac csudoku.scala'
 			 'scala main' 
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
			//Gera uma lista com todas as linhas em forma de string
			val rows = for(a <- 0 to 8) yield this.content.substring(a * 9, (a * 9) + 9)
			
			//Diz que a resposta é se não existem linhas com múltiplos elementos
			val answer = rows.filter((x) => verifyMultipleElements(x)).length == 0

			//Retorna a resposta
			answer
		}

		//Faz a verificação de pertinência múltipla em cada coluna do tabuleiro
		//Retorna true se está tudo conforme as regras
		def verifyColumns():Boolean = {
			//Gera uma lista com todas as colunas
			val columns = for(a <- 0 to 8) yield "" + this.content(a) + this.content(a + 9) +
					this.content(a + 18) + this.content(a + 27) +
					this.content(a + 36) + this.content(a + 45) +
					this.content(a + 54) + this.content(a + 63) +
					this.content(a + 72)

			//Diz que a resposta é se não existem colunas com múltiplos elementos
			val answer = columns.filter((x) => verifyMultipleElements(x)).length == 0
			
			//Retorna a resposta
			answer
		}

		//Faz a verificação de pertinência múltipla em cada quadrante do tabuleiro
		//Retorna true se está tudo conforme as regras
		def verifySquares():Boolean = {
			//Gera uma lista com os quadrantes superiores
			val upperSquares = for(a <- 0 to 2) yield "" + this.content(a*3) + 
					this.content(a*3 + 1) + this.content(a*3 + 2) + 
					this.content(a*3 + 9) + this.content(a*3 + 10) + 
					this.content(a*3 + 11) + this.content(a*3 + 18) + 
					this.content(a*3 + 19) + this.content(a*3 + 20)
				
			//Gera uma lista com os quadrantes intermediários
			val middleSquares = for(a <- 0 to 2) yield "" + this.content(a*3 + 27) + 
					this.content(a*3 + 28) + this.content(a*3 + 29) +
					this.content(a*3 + 36) + this.content(a*3 + 37) +
					this.content(a*3 + 38) + this.content(a*3 + 45) + 
					this.content(a*3 + 46) + this.content(a*3 + 47)

			//Gera uma lista com os quadrantes inferiores
			val lowerSquares = for(a <- 0 to 2) yield "" + this.content(a*3 + 54) + 
					this.content(a*3 + 55) + this.content(a*3 + 56) + 
					this.content(a*3 + 63) + this.content(a*3 + 64) + 
					this.content(a*3 + 65) + this.content(a*3 + 72) +
					this.content(a*3 + 73) + this.content(a*3 + 74)

			//Diz que a resposta é se não existem quadrantes com múltiplos elementos
			val answer = (upperSquares.filter((x) => verifyMultipleElements(x)).length == 0) &&
						(middleSquares.filter((x) => verifyMultipleElements(x)).length == 0) &&
						(lowerSquares.filter((x) => verifyMultipleElements(x)).length == 0)

			//Retorna a respsota
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
			//Gera uma lista com todos os conteúdos possíveis para próxima jogada
			val contents = for(value <- 1 to 9) yield this.content.replaceFirst("0", "" + value)
			//Gera uma lista de tabuleiros com os conteúdos gerados previamente
			val tables = for(content <- contents) yield new Table(content)

			//Assume como respostas apenas os tabuleiros gerados anteriormente que são válidos
			val answers = tables.filter((x) => x.isValid())
			
			//Retorna essa resposta em uma lista
			answers.toList
		}
	}

	//Retorna o tabuleiro em string, porém formatado em uma matriz 9x9
	def mkString():String = {

		//Função auxiliar que vai cortando o conteúdo em string de um tamanho dado
		//Ao final de cada string gerada, é adicionada a quebra de linha e concatenada com a anterior
		def aux(start:Int, length:Int, s:String):String = {
			if(start < this.content.length) {
				aux(start + length, length, s + this.content.substring(start, start + length) + "\n")
			}
			else {
				s
			}
		}

		aux(0,9,"")
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
	println("Segue em anexo um arquivo chamado exemplo.txt que mostra um exemplo de funcionamento")
	println("O exemplo.txt se baseia no tabuleiro dado por tabuleiro-exemplo.png")
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