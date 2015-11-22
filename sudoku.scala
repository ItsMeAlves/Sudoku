import org.jscala._
import org.jscala.JArray

object main extends App {
    val js = javascript {

    	def obterArrayDoHTML():JArray[JArray[Int]] = {
            val array:JArray[JArray[Int]] = JArray()
            var coluna:Int = 1
            var linha:Int = 1

            for(linha <- 1 to 9) {
                val aux:JArray[Int] = JArray();
                for(coluna <- 1 to 9) {
                    val element:String = (document.getElementById
                         ("l" + linha + "c" + coluna).textContent)
                    aux.push(parseInt(element))
                }
                array.push(aux)
            }
            if(!verificaValidade(array)) {
                console.log("Tabuleiro inválido! Recarregando...")
                document.location.reload(true)
            }
            return array
        } 

        def verificaValidade(vetor:JArray[JArray[Int]]):Boolean = {
            def verificaPertinenciaMultipla(array:JArray[Int]):Boolean = {                        
                var resposta = false
                if(array.length == 1) {
                    return resposta
                }

                val head = array.slice(0,1)
                val tail = array.slice(1,array.length)

                if(tail.indexOf(head(0)) > -1) {
                    resposta = true;
                    return resposta;
                } else {
                  return verificaPertinenciaMultipla(tail);
                }
            }

            var a:Boolean = true
            var b:Boolean = true
            var c:Boolean = true

            //Verifica linhas
            var linha = 0
            for(linha <- 0 to 8) {

                if(verificaPertinenciaMultipla(vetor(linha))) {
                    console.log("linha invalido")
                    a = a && false
                }
                else {
                    console.log("linha valido")
                    a = a && true
                }
            }

            //Verifica colunas
            var coluna = 0
            var colunas:JArray[Int] = JArray()
            for(coluna <- 0 to 8) {
                colunas = JArray()
                for(linha <- 0 to 8) {
                    colunas.push(vetor(linha)(coluna))
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
            while(ka < 3 || kb < 2) {
                if(ka >= 3) {
                    ka = 0
                    kb += 1
                }

                quadrantes = JArray()
                for(linha <- ka*3 to ka*3 + 2) {
                    for(coluna <- kb*3 to kb*3 + 2) {
                        quadrantes.push(vetor(linha)(coluna))
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

            return a && b && c
        } 

        def verificaSolucao(vetor:JArray[JArray[Int]]):Boolean = {
            var a = 0
            var b = 0
            for(a <- 0 to 8) {
                for(b <- 0 to 8) {
                    if(isNaN(vetor(a)(b))) {
                        return false;
                    }
                }
            }
            true
        }

        def listaJogadasValidas(v:JArray[JArray[Int]]):JArray[JArray[JArray[Int]]] = {
            if(verificaSolucao(v)) {
                console.log("sem jogadas válidas")
                return JArray()
            }            
            else {
                val possibilidades:JArray[JArray[JArray[Int]]] = JArray()
                
                var linha = 0
                var coluna = 0
                var trava = true

                while(trava) {
                    if(isNaN(v(linha)(coluna))) {
                        trava = false
                    }
                    else {
                        coluna += 1
                    }
                    if(coluna > 8) {
                        coluna = 0
                        linha += 1
                    }
                }

                for(valor <- 1 to 9) {
                    var novaPossibilidade = v.slice(0, v.length)
                    novaPossibilidade(linha)(coluna) = valor
                    if(verificaValidade(novaPossibilidade)) {
                        possibilidades.push(novaPossibilidade)
                    }
                }

                return possibilidades
            }
        }

        def resolve(x:JArray[JArray[Int]]):Unit = {
            def atualizaHTML(vetor:JArray[JArray[Int]]) = {
                var linha = 0
                var coluna = 0
                for(linha <- 0 to 8) {
                    for(coluna <- 0 to 8) {
                        val element = document.getElementById("l" + (linha+1) + "c" + (coluna+1))
                        element.textContent = "" + vetor(linha)(coluna)
                    }
                }
            }

            if(verificaSolucao(x)) {
                console.log("resposta!")
                atualizaHTML(x)
            }
            else {
                var a = 0
                var proximasJogadas = listaJogadasValidas(x)
                for(a <- 0 to proximasJogadas.length - 1) {
                    resolve(proximasJogadas(a))
                }
            }
        }

        val btn = document.getElementById("btn-principal")
        btn.addEventListener("click", () => {
          if(btn.textContent != "Reiniciar!") {
                btn.textContent = "Reiniciar!";
                console.log("resolve");
                resolve(obterArrayDoHTML())
    		}
    		else {
    			btn.textContent = "Iniciar!";
    			document.location.reload(true);
    		}
         })
    }
    println(js.asString)
}