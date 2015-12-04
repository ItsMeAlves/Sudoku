{
  function Tabuleiro() {
    this.array = [];
    this.obterArrayDoHTML = function (inicial) {
      for (var linha = 1; linha <= 4; ++linha) {
        var aux = [];
        for (var coluna = 1; coluna <= 4; ++coluna) {
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
      for (var linha = 0; linha <= 3; ++linha) for (var coluna = 0; coluna <= 3; ++coluna) {
        var element = document.getElementById((("l" + (linha + 1)) + "c") + (coluna + 1));
        element.textContent = "" + this.array[linha][coluna];
      };
    };
    this.verificaSolucao = function () {
      for (var a = 0; a <= 3; ++a) for (var b = 0; b <= 3; ++b) if (isNaN(this.array[a][b])) return false;
      return true;
    };
    this.verificaValidade = function () {
      function verificaPertinenciaMultipla(array) {
        var resposta = false;
        if (array.length == 1) return resposta;
        var head = array.slice(0, 1);
        var tail = array.slice(1, array.length);
        if (tail.indexOf(head[0]) > -1) {
          resposta = true;
          return resposta;
        } else return verificaPertinenciaMultipla(tail);
      };
      var a = true;
      var b = true;
      var c = true;
      for (var linha = 0; linha <= 3; ++linha) if (verificaPertinenciaMultipla(this.array[linha])) {
        console.log("linha invalida");
        a = a && false;
      } else {
        console.log("linha valida");
        a = a && true;
      };
      var colunas = [];
      for (var coluna = 0; coluna <= 3; ++coluna) {
        colunas = [];
        for (var linha = 0; linha <= 3; ++linha) colunas.push(this.array[linha][coluna]);
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
      while ((ka < 2) || (kb < 1)) {
        if (ka >= 2) {
          ka = 0;
          kb = kb + 1;
        };
        quadrantes = [];
        for (var linha = ka * 2; linha <= ((ka * 2) + 1); ++linha) for (var coluna = kb * 2; coluna <= ((kb * 2) + 1); ++coluna) quadrantes.push(this.array[linha][coluna]);
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
    this.resolve = function () {
      this.atualizaHTML();
      if (this.verificaSolucao()) {
        console.log("sem mais jogadas");
        this.atualizaHTML();
      } else for (var a = 1; a <= 4; ++a) for (var b = 1; b <= 4; ++b) {
        var celula = document.getElementById((("l" + a) + "c") + b);
        if (celula.textContent == "NaN") {
          var tabuleiro = null;
          for (var valor = 1; valor <= 4; ++valor) {
            tabuleiro = new Tabuleiro();
            tabuleiro.obterArrayDoHTML(false);
            tabuleiro.array[a - 1][b - 1] = valor;
            if (tabuleiro.verificaValidade()) tabuleiro.resolve();
          };
        };
      };
    };
  };
  var btn = document.getElementById("btn-principal");
  btn.addEventListener("click", function () {
      if (btn.textContent != "Reiniciar!") {
        btn.textContent = "Reiniciar!";
        var tabuleiro = new Tabuleiro();
        tabuleiro.obterArrayDoHTML(true);
        tabuleiro.atualizaHTML();
        tabuleiro.resolve();
      } else {
        btn.textContent = "Iniciar!";
        document.location.reload(true);
      };
    });
}