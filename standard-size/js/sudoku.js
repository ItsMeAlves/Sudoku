{
  function Tabuleiro(fonte) {
    this.fonte = fonte;
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
    this.obterArrayDaFonte = function () {
      if (this.fonte != null) for (var linha = 0; linha <= 8; ++linha) this.array.push(this.fonte.array[linha].concat([]));
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
        if (tail.indexOf(head[0]) > -1) {
          resposta = true;
          return resposta;
        } else return verificaPertinenciaMultipla(tail);
      };
      var a = true;
      var b = true;
      var c = true;
      for (var linha = 0; linha <= 8; ++linha) if (verificaPertinenciaMultipla(this.array[linha])) {
        console.log("linha invalida");
        a = a && false;
      } else {
        console.log("linha valida");
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
    this.resolve = function () {
      this.atualizaHTML();
      if (this.verificaSolucao()) {
        console.log("sem mais jogadas");
        this.atualizaHTML();
      } else {
        var todasAsPosicoes = [];
        for (var a = 0; a <= 8; ++a) for (var b = 0; b <= 8; ++b) if (isNaN(this.array[a][b])) {
          var respostas = [];
          var tabuleiro = null;
          for (var valor = 1; valor <= 9; ++valor) {
            tabuleiro = new Tabuleiro(this);
            tabuleiro.obterArrayDaFonte();
            tabuleiro.array[a][b] = valor;
            if (tabuleiro.verificaValidade()) respostas.push(tabuleiro);
          };
          todasAsPosicoes.push(respostas);
        };
        for (var respostasIdx = 0, respostas = todasAsPosicoes[respostasIdx]; respostasIdx < todasAsPosicoes.length; respostas = todasAsPosicoes[++respostasIdx]) for (var tIdx = 0, t = respostas[tIdx]; tIdx < respostas.length; t = respostas[++tIdx]) t.resolve();
      };
    };
  };
  var btn = document.getElementById("btn-principal");
  btn.addEventListener("click", function () {
      if (btn.textContent != "Reiniciar!") {
        btn.textContent = "Reiniciar!";
        var tabuleiro = new Tabuleiro(null);
        tabuleiro.obterArrayDoHTML(true);
        tabuleiro.atualizaHTML();
        tabuleiro.resolve();
      } else {
        btn.textContent = "Iniciar!";
        document.location.reload(true);
      };
    });
}