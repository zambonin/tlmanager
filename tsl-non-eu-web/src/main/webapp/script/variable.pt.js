digitTslWeb.constant('appConstant', {

  confirmationDirective: {
    genericConfirmation: "Você confirma esta ação?"
  },

  digitalIdentity: {
    undefinedDigitalIdentity: "Identidade digital não definida.",
    certificateFileInvalid: "Arquivo de certificado é inválido ou está corrompido.",
    pointerToOthers: "POINTERS_TO_OTHER_TSL"
  },

  errorFactory: {
    errorOccured: "Um erro ocorreu."
  },

  extensionFactory: {
    addExtensionFailure: "Falha no processo de adição de extensão.",
    editExtensionFailure: "Falha no processo de edição de extensão.",
    keyUsageList: [
      "digitalSignature", "nonRepudiation", "keyEncipherment", "dataEncipherment",
      "keyAgreement", "keyCertSign", "crlSign", "encipherOnly", "decipherOnly"
    ],
    takenOverBy: "Assumido por (TakenOverBy)",
    qualificationExtension: "Qualificações (Qualifications)",
    additionnalService: "Informação adicional de serviço (additionalServiceInformation)",
    expiredCertificate: "Informação de revogação de certificados expirados (expiredCertsRevocationInfo)",
    anyType: "Extensão (AnyType)",
    anyTypeNotSupported: "A edição de extensões do tipo AnyType não é suportada pela aplicação no momento.",
    extUndefined: "Extensão (indefinida)"
  },

  nexuFactory: {
    oldVersionError: '<p>Você está utilizando uma versão não suportada do NexU.</p>'
      + '<p>A última versão da aplicação pode ser encontrada aqui: <a href="%NEXU_URL%">download do NexU</a>.</p>',
    notInstalledError: '<p>O NexU não está executando em seu computador ou não foi instalado ainda.</p>'
      + '<p>Se você deseja assinar uma Lista Confiável diretamente por esta aplicação, você precisa baixar e/ou executar o NexU no seu computador. '
      + 'O NexU é um módulo de geração de assinaturas de código aberto que pode ser encontrado aqui: <a href="%NEXU_URL%">download do NexU</a>.</p>'
      + '<p>Se você não deseja utilizar o NexU, como alternativa, você pode exportar a Lista Confiável, assiná-la com a aplicação de sua escolha, e importá-la novamente nesta aplicação.</p>',
    certificateError: "<p>Um erro ocorreu na conexão com o smartcard/token. Por favor, tente novamente.<p/>"
      + "<div>Se o problema persistir, por favor contate o suporte.</div>",
    signatureError: "<p>Um erro ocorreu na geração da assinatura. Por favor, tente novamente.<p/>"
      + "<div>Se o problema persistir, por favor contate o suporte.</div>",
    noProductFound: "no.product.found",
    userCancelled: "user.cancel",
    userCancelledMessage: "O usuário cancelou a confecção da assinatura.",
    certificateRequestError: "Um erro ocorreu na obtenção do certificateRequest.",
    noProductFoundMessage: "<p>Nenhum dispositivo encontrado, por favor certifique-se que o smartcard/token está conectado e tente novamente.</p>"
      + "<div>Se o problema persistir, por favor contate o suporte.</div>",
    getUrlError: "Falha na obtenção dos arquivos do NexU pelo URL especificado.",
    getSealError: "Falha na obtenção da lista de selos."
  },

  nexuLoading: {
    checkVersion: "Verificando versão do NexU.",
    getSmart: "Obtendo certificados.",
    getTbs: "Obtendo dados para assinatura.",
    finalize: "Finalizando processo.",
    chooseCert: "Você deve escolher um certificado associado a uma chave privada para assinar.",
    sigError: "<p>Um erro ocorreu no processo de assinatura com o smartcard/token. Por favor, tente novamente.</p>"
      + "<div>Se o problema persistir, por favor contate o suporte.</div>",
    storeNotification: "Armazenando notificação."
  },

  trustedListFactory: {
    getTlError: "A Lista Confiável requisitada não existe.",
    getSignatureError: "Falha no carregamento de informações da assinatura.",
    getChangeError: "Falha no carregamento de mudanças.",
    getCheckError: "Falha no carregamento de inspeções.",
    getAllDraftsError: "Falha na obtenção dos rascunhos.",
    getAllCheckError: "Falha na execução das inspeções.",
    getDraftEditionCheckError: "Falha na obtenção das inspeções habilitadas.",
    checkProviderError: "Falha nas inspeções realizadas no Prestador de Serviço de Confiança.",
    getLastEditedError: "Falha no carregamento da data de última edição.",
    conflictTlFailure: "Falha na resolução de conflitos de edição da Lista Confiável.",
    checkSignatureFailure: "Falha na verificação da assinatura.",
    notifiedPointerFailure: "Falha na obtenção de notificações.",
    switchCheckToRunError: "Falha na mudança da execução de inspeção.",
    tlIdIncorrect: "O identificador da Lista Confiável no URL está incorreto. Insira um número natural ou código de país.",
    getChangesDraftError: "Um erro ocorreu na obtenção de mudanças.",
    signatureChangeError: "Um erro ocorreu na obtenção de mudanças na assinatura."
  },

  cookieFactory: {
    validityError: "O repositório requisitado não existe.",
    validityFailure: "Falha na verificação da validade do repositório.",
    createFailure: "Falha na criação de novo repositório.",
    createCookie: "<div> <label><u>Criação de um repositório para rascunhos de Listas Confiáveis</u></label></div>"
      + "<div>Um repositório pessoal de rascunhos de Listas Confiáveis foi criado para você sob o menu <b>“Meus rascunhos”</b>. "
      + "Este repositório armazenará todos os rascunhos criados por você.</div>"
      + "<div>Um identificador único foi gerado para esse repositório. "
      + "Este identificador pode ser visto no URL da página, na barra de endereço de seu navegador.</div><br/>"
      + "<div> <label><u>Como acessá-lo</u></label></div>"
      + "<p>Para sua conveniência, um <b><i>cookie</i></b> foi configurado neste navegador, que se refere ao repositório “Meus rascunhos”. "
      + "Assim, quando você retornar à aplicação, encontrará o mesmo repositório com seus rascunhos nele.</p>"
      + "<div>Porém, observe que <i>cookies</i> podem ser removidos por eventuais manutenções no seu computador. "
      + "Portanto, é fortemente recomendável que você <u>favorite</u> esta página para referências futuras.</div>"
      + "<div>Uma outra maneira de lembrar da página é copiar e colar o URL em um documento, e-mail ou outros meios de armazenamento de informações textuais.</div>"
      + "<div>Com este URL, você pode:</div>"
      + "<div class='alinea'>-	<b>Acessar</b> o mesmo repositório no futuro, e encontrar os rascunhos que você criou.</div>"
      + "<div class='alinea'>-	<b>Compartilhar</b> seu repositório com outras pessoas (por exemplo, colegas de trabalho), enviando-as o URL.</div><br/>"
      + "<div> <label><u>Compartilhamento de um repositório</u></label></div>"
      + "<p>Compartilhar seu repositório com outros pode ser um jeito fácil de trabalhar juntos no conteúdo de uma nova Lista Confiável antes de sua publicação. "
      + "Porém, se você compartilhou seu repositório, observe que qualquer um com conhecimento do URL pode <b>navegar, editar, remover e assinar</b> seus rascunhos. "
      + "Assim, se você planeja publicar um rascunho como a Lista Confiável oficial de seu país, é fortemente recomendável que uma cópia local seja mantida em seu computador. "
      + "Deste modo, é possível impedir que outros modifiquem sua Lista Confiável acidentalmente ou em sigilo, logo antes de sua assinatura e publicação.</p>"
      + "<p>Se você deseja <b>parar de compartilhar</b> seu repositório com outros, sugere-se a geração de um novo repositório (veja o botão na página “Meus rascunhos”). "
      + "As pessoas ainda poderão acessar o repositório compartilhado antigo, mas você trabalhará em um novo repositório pessoal.</p>"
      + "<div><label><u>Política de retenção</u></label></div>"
      + "<div>O repositório será mantido por <b>2 (dois) meses</b> após a deleção do último rascunho de Lista Confiável no mesmo. "
      + "Desde que o repositório contenha pelo menos um rascunho, ainda continuará acessível (observe a política de retenção de Listas Confiáveis).</div>",
    replaceStore: "<div>De acordo com a informação presente no <i>cookie</i>, parece que o repositório que você está tentando acessar não é o seu repositório “Meus rascunhos” usual. "
      + "Por favor, clique em “OK” se você deseja substituir o seu repositório pelo designado neste URL.</div>"
      + "<div>Clicar em “Cancelar” impedirá esta ação, para você poder, por exemplo, favoritar seu repositório atual para uso futuro antes de trocar para o novo.</div>",
    invalidUrl: "O repositório “Meus rascunhos” que você está tentando acessar expirou.",
    invalidDraftStore: "Seu repositório “Meus rascunhos” usual que você está tentando acessar expirou.",
    newStore: "<div>Esta operação criará um repositório novo e abandonará o atual. "
      + "As pessoas com as quais você <b>compartilhou</b> o repositório atual ainda poderão acessá-lo, mas você agora trabalhará em um repositório pessoal novo e <b>vazio</b>.</div>"
      + "Se você deseja acessar o repositório atual no futuro, por favor <b>favorite</b> esta página antes de continuar. "
      + "Se você deseja transferir alguns de seus rascunhos para o repositório novo, por favor <b>exporte-os</b> antes, e importe-os no novo repositório após sua criação."
      + "<p>Você deseja <b>continuar</b> mesmo assim?</p>",
    repository: "Informação de repositórios"
  },

  httpStatus: {
    notAuthorized: "Você não está autorizado a executar a operação atual ou sua sessão expirou.",
    tlNotFound: "Arquivo de Lista Confiável não encontrado. Se o problema persistir, por favor contate o suporte.",
    titleApplicationError: "Erro na aplicação",
    titleNotAuthorized: "Não autorizado",
    titleNotFound: "Não encontrado",
    titleBadRequest: "Requisição incorreta",
    titleForbidden: "Proibido",
    titleError: "Erro"
  },

  modalTitle: {
    warning: "Aviso",
    applicationError: "Erro na aplicação",
    signatureError: "Erro na assinatura",
    information: "Informação",
    repository: "Informação de repositórios",
    confirmation: "Confirmação"
  },

  pointerController: {
    digitalDouble: "O certificado já existe nas identidades digitais: ",
    digitalMultipleDouble: "Os certificados já existem nas identidades digitais: ",
    getDigital: "Esta operação substituirá as identidades digitais existentes pelos certificados de assinante, você deseja proceder?",
    getDigitalError: "Falha na obtenção de identidades digitais."
  },

  systemController: {
    loadingFailure: "Falha no carregamento.",
    rulesFailure: "Falha na validação de regras.",
    signatureFailure: "Falha na verificação da assinatura.",
    retentionFailure: "Falha na execução da política de retenção.",
    serviceDataFailure: "Falha no recarregamento dos dados do serviço.",
    signatureAlertFailure: "Falha na obtenção de avisos sobre a assinatura.",
    cacheCountriesFailure: "Falha na limpeza da cache de países.",
    cachePropertiesFailure: "Falha na limpeza da cache de propriedades.",
    cacheCheckFailure: "Falha na limpeza da cache de inspeções.",
    cacheCountriesSuccess: "Limpeza da cache de países feita corretamente.",
    cachePropertiesSuccess: "Limpeza da cache de propriedades feita corretamente.",
    cacheChecksSuccess: "Limpeza da cache de inspeções feita corretamente."
  },

  userController: {
    userAdded: "Usuário de nome “%NAME%” adicionado.",
    updateSuccessful: "Atualização feita com sucesso."
  },

  retentionController: {
    loading_data: "Carregando informações...",
    loading_failure: "Falha no carregamento de informações.",
    delete_draftstore_failure: "Falha na deleção dos dados."
  },

});
