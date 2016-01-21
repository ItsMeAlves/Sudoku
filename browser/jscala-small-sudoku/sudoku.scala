//lcsm [LPF 2015.2]
/*
 *  Código de resolução de Sudoku
 *  Implementação com HTML e JScala
 *  Aluno: Gabriel Alves de Lima (http://github.com/ItsMeAlves/Sudoku)
 *  Para execução, executar o SBT (Scala Build Tool) na pasta raiz do projeto
 *  Executar 'run' e o código JScala vai atualizar o js/sudoku.js
 *  Nada mais é necessário, visto que o o js/sudoku.js já está incluido no HTML
 */

//Imports para o funcionamento da compilação do JScala
import org.jscala._
import org.jscala.JArray

//Importa classes para imprimir o código javascript gerado pelo JScala
import java.io.{File, FileWriter}   

//O objeto main herdando de App faz com que ele implemente um método main
object main extends App {

    //Aqui vai o código JScala
    val js = javascript {

        //Cria a classe Tabuleiro, que vai representar o Sudoku
        class Tabuleiro() {
            //Este array representa o tabuleiro desenhado no HTML
            val array:JArray[JArray[Int]] = JArray()  

            //Obtém os valores iniciais definidos no HTML por um pequeno javascript
            //Recebe um booleano para verificar se essa operação é o começo da execução
            //Os valores obtidos são guardados no atributo array
            //Os valores não iniciados são do tipo NaN (not a number)
            def obterArrayDoHTML(inicial:Boolean) = {
                for(linha <- 1 to 4) {
                    val aux:JArray[Int] = JArray();
                    for(coluna <- 1 to 4) {
                        val element:String = (document.getElementById
                             ("l" + linha + "c" + coluna).textContent)
                        aux.push(parseInt(element))
                    }
                    this.array.push(aux)
                }
                if(inicial) {
                    //Caso os valores iniciais sejam contra as regras, a página é recarregada
                    if(!this.verificaValidade()) {
                        console.log("Tabuleiro inválido! Recarregando...")
                        document.location.reload(true)
                    }
                }
            }

            //A partir dos valores do atributo array, a tabela no HTML será atualizada
            def atualizaHTML() = {
                var linha = 0
                var coluna = 0
                for(linha <- 0 to 3) {
                    for(coluna <- 0 to 3) {
                        val element = document.getElementById("l" + (linha+1) + "c" + (coluna+1))
                        element.textContent = "" + this.array(linha)(coluna)
                    }
                }
            }

            //Verifica se o tabuleiro está totalmente preenchido
            //Se estiver totalmente preenchido, retorna true
            def verificaSolucao():Boolean = {
                for(a <- 0 to 3) {
                    for(b <- 0 to 3) {
                        if(isNaN(this.array(a)(b))) {
                            return false;
                        }
                    }
                }
                true
            }

            //Verifica as regras, quanto a repetição dos valores nas linhas colunas e quadrantes
            def verificaValidade():Boolean = {

                //Função que recebe um array e verifica recursivamente se algum elemento está repetido
                def verificaPertinenciaMultipla(array:JArray[Int]):Boolean = {                        
                    var resposta = false
                    if(array.length == 1) { 
                        return resposta     //Retorna falso se o vetor é de um só elemento
                    }

                    //Divide o vetor em cabeça (head) e cauda (tail)
                    val head = array.slice(0,1)
                    val tail = array.slice(1,array.length)

                    //faz a verificação de pertinência da cabeça na cauda
                    if(tail.indexOf(head(0)) > -1) {
                        resposta = true;
                        return resposta;
                    } else {
                      return verificaPertinenciaMultipla(tail);
                    }
                }

                //Variáveis que vão responder a validade de cada regra
                var a:Boolean = true    //Validade das linhas do tabuleiro
                var b:Boolean = true    //Validade das colunas do tabuleiro
                var c:Boolean = true    //Validade dos quadrantes do tabuleiro

                //Verifica linhas
                for(linha <- 0 to 3) {
                    if(verificaPertinenciaMultipla(this.array(linha))) {
                        console.log("linha invalida")
                        a = a && false
                    }
                    else {
                        console.log("linha valida")
                        a = a && true
                    }
                }

                //Verifica colunas
                var colunas:JArray[Int] = JArray()
                for(coluna <- 0 to 3) {
                    //Como as colunas não são arrays, é preciso criar esse array
                    colunas = JArray()
                    for(linha <- 0 to 3) {
                        colunas.push(this.array(linha)(coluna))
                    }

                    if(verificaPertinenciaMultipla(colunas)) {
                        console.log("coluna invalida")
                        b = b && false
                    }
                    else {
                        console.log("coluna valida")
                        b = b && true
                    }
                }

                //Verifica quadrantes
                var quadrantes:JArray[Int] = JArray()
                var ka = 0
                var kb = 0
                while(ka < 2 || kb < 1) {
                    if(ka >= 2) {
                        ka = 0
                        kb += 1
                    }

                    //Cria arrays para cada quadrante
                    quadrantes = JArray()
                    for(linha <- ka*2 to ka*2 + 1) {
                        for(coluna <- kb*2 to kb*2 + 1) {
                            quadrantes.push(this.array(linha)(coluna))
                        }
                    }

                    if(verificaPertinenciaMultipla(quadrantes)) {
                        console.log("quadrante invalido");
                        c = c && false
                    }
                    else {
                        console.log("quadrante valido");
                        c = c && true
                    }

                    ka += 1
                }

                //Retorna true apenas se todas as regras estiverem satisfeitas
                return a && b && c
            }

            //Função que resolve o tabuleiro, usando o algoritmo de backtracking
            def resolve():Unit = {
                this.atualizaHTML()

                //Se o tabuleiro está preenchido, para a resolução e atualiza o HTML
                if(this.verificaSolucao()) {
                    console.log("sem mais jogadas")
                    this.atualizaHTML()
                }
                else {
                    //Procura no HTML a primeira posição vazia para testar as soluções
                    for(a <- 1 to 4) {
                        for(b <- 1 to 4) {
                            var celula = document.getElementById("l" + a + "c" + b)

                            //Ao encontrar essa primeira posição, tenta-se a resolução
                            //Cria-se tabuleiros com todas as próximas jogadas possíveis
                            //Para todas essas próximas jogadas, tenta-se uma resolução
                            if(celula.textContent == "NaN") {
                                var tabuleiro:Tabuleiro = null

                                //Valores de jogadas possíveis
                                for(valor <- 1 to 4) {
                                    tabuleiro = new Tabuleiro()
                                    tabuleiro.obterArrayDoHTML(false)
                                    tabuleiro.array(a-1)(b-1) = valor
                                    if(tabuleiro.verificaValidade()) {
                                        tabuleiro.resolve()   //Resolve cada jogada válida
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Adiciona um click listener no botão de resolução
        //Ao clicar no botão, a função abaixo é executada
        val btn = document.getElementById("btn-principal")
        btn.addEventListener("click", () => {
            //Se o botão for para iniciar, muda o título dele e começa a resolução
            if(btn.textContent != "Reiniciar!") {
                btn.textContent = "Reiniciar!";
                val tabuleiro = new Tabuleiro()     //Tabuleiro novo
                tabuleiro.obterArrayDoHTML(true)    //Lê os valores iniciais
                tabuleiro.atualizaHTML()            //Atualiza o html para iterações da resolução
                tabuleiro.resolve()                 //resolve o tabuleiro inicial
            }
            else {  //Se for para reiniciar, recarrega a página
                btn.textContent = "Iniciar!";
                document.location.reload(true);
            }
        })
    }
    val file = new File("js/sudoku.js") //Cria um arquivo na pasta js com o nome sudoku
    val fw = new FileWriter(file)   //Cria um FileWriter
    fw.write(js.asString)           //Escreve no arquivo o código em javascript
    println(js.asString)            //imprime o código javascript no console
    fw.close()                      //Fecha o arquivo para descarga no FileWriter
}
