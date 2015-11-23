{
  function Tabuleiro() {
    this.array = [];
    this.obterArrayDoHTML = function (inicial) {
      for (var linha = 1; linha <= 9; ++linha) {
        var aux = [];
        for (var coluna = 1; coluna <= 9; ++coluna) {
          var element = document.getElementById((("l" + linha) + "c") + coluna).textContent;
          aux.push(parseInt(element));
        };
        this.array.push(aux);
      };
      if (inicial) if (!this.verificaValidade()) {
        console.log("Tabuleiro inválido! Recarregando...");
        document.location.reload(true);
      };
    };
    this.atualizaHTML = function () {
      var linha = 0;
      var coluna = 0;
      for (var linha = 0; linha <= 8; ++linha) for (var coluna = 0; coluna <= 8; ++coluna) {
        var element = document.getElementById((("l" + (linha + 1)) + "c") + (coluna + 1));
        element.textContent = "" + this.array[linha][coluna];
      };
    };
    this.verificaSolucao = function () {
      for (var a = 0; a <= 8; ++a) for (var b = 0; b <= 8; ++b) if (isNaN(this.array[a][b])) return false;
      return true;
    };
    this.verificaValidade = function () {
      function verificaPertinenciaMultipla(array) {
        var resposta = false;
        if (array.length == 1) return resposta;
        var head = array.slice(0, 1);
        var tail = array.slice(1, array.length);
        if ((tail.indexOf(head[0]) > -1)) {
          resposta = true;
          return resposta;
        } else return verificaPertinenciaMultipla(tail);
      };
      var a = true;
      var b = true;
      var c = true;
      for (var linha = 0; linha <= 8; ++linha) if (verificaPertinenciaMultipla(this.array[linha])) {
        console.log("linha invalido");
        a = a && false;
      } else {
        console.log("linha valido");
        a = a && true;
      };
      var colunas = [];
      for (var coluna = 0; coluna <= 8; ++coluna) {
        colunas = [];
        for (var linha = 0; linha <= 8; ++linha) colunas.push(this.array[linha][coluna]);
        if (verificaPertinenciaMultipla(colunas)) {
          console.log("coluna invalida");
          b = b && false;
        } else {
          console.log("coluna valida");
          b = b && true;
        };
      };
      var quadrantes = [];
      var ka = 0;
      var kb = 0;
      while ((ka < 3) || (kb < 2)) {
        if (ka >= 3) {
          ka = 0;
          kb = kb + 1;
        };
        quadrantes = [];
        for (var linha = ka * 3; linha <= ((ka * 3) + 2); ++linha) for (var coluna = kb * 3; coluna <= ((kb * 3) + 2); ++coluna) quadrantes.push(this.array[linha][coluna]);
        if (verificaPertinenciaMultipla(quadrantes)) {
          console.log("quadrante invalido");
          c = c && false;
        } else {
          console.log("quadrante valido");
          c = c && true;
        };
        ka = ka + 1;
      };
      return (a && b) && c;
    };
    this.jogadasSeguintes = function () {
      if (this.verificaSolucao()) {
        console.log("sem mais jogadas");
        return [];
      } else {
        for (var a = 1; a <= 9; ++a) for (var b = 1; b <= 9; ++b) {
          var celula = document.getElementById((("l" + a) + "c") + b);
          if (celula.textContent == "NaN") {
            var respostas = [];
            var tabuleiro = null;
            for (var valor = 1; valor <= 9; ++valor) {
              tabuleiro = new Tabuleiro();
              celula.textContent = "" + valor;
              tabuleiro.obterArrayDoHTML(false);
              if (tabuleiro.verificaValidade()) respostas.push(tabuleiro);
            };
            return respostas;
          };
        };
        return [];
      };
    };
  };
  function resolve(x) {
    x.atualizaHTML();
    if (x.verificaSolucao()) {
      console.log("resposta!");
      x.atualizaHTML();
    } else {
      var jogadas = x.jogadasSeguintes();
      if (jogadas.length > 0) for (var tIdx = 0, t = jogadas[tIdx]; tIdx < jogadas.length; t = jogadas[++tIdx]) {
        console.log("tentando");
        resolve(t);
      };
    };
  };
  var btn = document.getElementById("btn-principal");
  btn.addEventListener("click", function () {
      if (btn.textContent != "Reiniciar!") {
        btn.textContent = "Reiniciar!";
        var tabuleiro = new Tabuleiro();
        tabuleiro.obterArrayDoHTML(true);
        resolve(tabuleiro);
      } else {
        btn.textContent = "Iniciar!";
        document.location.reload(true);
      };
    });
}