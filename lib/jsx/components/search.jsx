/** @jsx React.DOM */

var statusMap;

statusMap = {
  init: {
    icon: "time",
    detail: function() {
      return "Iniciando a busca...";
    }
  },
  info: {
    icon: "time",
    detail: function() {
      return "Carregando informações do vídeo...";
    }
  },
  upload: {
    icon: "upload",
    detail: function(data) {
      return "Enviando " + data.upload + " ...";
    }
  },
  search: {
    icon: "search",
    detail: function(data) {
      return "Buscando legendas nos indiomas: " + data.search;
    }
  },
  download: {
    icon: "download",
    detail: function(data) {
      return "Baixando legenda do servidor: " + data.download.source.name() + "...";
    }
  },
  downloaded: {
    icon: "check",
    detail: function(data) {
      var website = data.download.source.website();

      return "Baixado do servidor " + data.download.source.name() + " (" + website + ")";
    }
  },
  notfound: {
    icon: "error",
    detail: function() {
      return "Nenhuma legenda encontrada, tente novamente mais tarde";
    }
  },
  unchanged: {
    icon: "check-small",
    detail: function() {
      return "Você já tem a legenda no seu indioma favorito";
    }
  },
  uploaded: {
    icon: "check",
    detail: function() {
      return "Suas legendas locais para esse vídeo foram compartilhadas!";
    }
  },
  share: {
    icon: "upload",
    detail: function() {
      return "Compartilhando as legendas desse vídeo...";
    }
  },
  error: {
    icon: "error",
    detail: function(data) {
      return "Erro: " + data.error;
    }
  }
};

module.exports = function (React, _) {
  var possibleStatus = [
    'init',
    'info',
    'upload',
    'search',
    'download',
    'downloaded',
    'notfound',
    'unchanged',
    'uploaded',
    'share',
    'error'];

  return React.createClass({
    displayName: 'Search',

    iconPath: function (icon) {
      return "images/icon-" + icon + ".svg";
    },

    normalizePath: function (path) {
      return _.last(path.split('/'));
    },

    render: function () {
      var data = this.props.data;
      var status = data.status;

      var info = statusMap[status];
      var className = "search flex-row " + status;

      return (
        <div className={className}>
          <img src={this.iconPath(info.icon)} className="status" />
          <div className="info flex">
            <div className="path">{this.normalizePath(data.path)}</div>
            <div className="detail">{info.detail(data)}</div>
          </div>
          <div className="actions">
            <div className="close"><img src="images/icon-close.svg"/></div>
            <div className="view"><img src="images/icon-view.svg"/></div>
            <div className="alternatives"><img src="images/icon-plus.svg"/></div>
          </div>
        </div>
      );
    }
  });
};
