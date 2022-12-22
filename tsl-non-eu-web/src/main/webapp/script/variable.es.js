digitTslWeb.constant('appConstant', {

  confirmationDirective: {
    genericConfirmation: "¿Confirma esta acción?"
  },

  digitalIdentity: {
    undefinedDigitalIdentity: "Identidad digital indefinida.",
    certificateFileInvalid: "El archivo del certificado no es válido o está dañado.",
    pointerToOthers: "POINTERS_TO_OTHER_TSL"
  },

  errorFactory: {
    errorOccured: "Se ha producido un error."
  },

  extensionFactory: {
    addExtensionFailure: "Fallo en el proceso de añadir extensión.",
    editExtensionFailure: "Falló el proceso de edición de la extensión.",
    keyUsageList: [
      "digitalSignature", "nonRepudiation", "keyEncipherment", "dataEncipherment",
      "keyAgreement", "keyCertSign", "crlSign", "encipherOnly", "decipherOnly"
    ],
    takenOverBy: "Tomado por (TakenOverBy)",
    qualificationExtension: "Cualificaciones (Qualifications)",
    additionnalService: "Información adicional sobre el servicio (additionalServiceInformation)",
    anyType: "Extensión (AnyType)",
    anyTypeNotSupported: "La aplicación no admite la edición de extensiones de tipo AnyType en este momento.",
    extUndefined: "Extensión (indefinida)"
  },

  nexuFactory: {
    oldVersionError: '<p>Está utilizando una versión no compatible de NexU.</p>'
      + '<p>La última versión de la aplicación puede consultarse aquí: <a href="%NEXU_URL%">download NexU</a>.</p>',
    notInstalledError: '<p>NexU no se está ejecutando en su PC o aún no se ha instalado.</p>'
      + '<p>Si desea suscribirse a una Lista de Confianza directamente a través de esta aplicación, deberá descargar y/o ejecutar NexU en su PC. '
      + 'NexU es un módulo de generación de firmas de código abierto que puede encontrarse aquí: <a href="%NEXU_URL%">download NexU</a>.</p>'
      + '<p>Si no desea utilizar NexU, como alternativa, puede exportar la Lista de Confianza, firmarla con la aplicación de su elección e importarla de nuevo a esta aplicación.</p>',
    certificateError: "<p>Se ha producido un error al conectar con la smartcard/token. Por favor, inténtelo de nuevo.<p/>"
      + "<div>Si el problema persiste, póngase en contacto con el servicio de asistencia.</div>",
    signatureError: "<p>Ha ocurrido un error al generar la firma. Por favor, inténtelo de nuevo.<p/>"
      + "<div>Si el problema persiste, póngase en contacto con el servicio de asistencia.</div>",
    noProductFound: "no.product.found",
    userCancelled: "user.cancelled",
    userCancelledMessage: "El usuario canceló la realización de la firma.",
    certificateRequestError: "Se ha producido un error al obtener el certificateRequest.",
    noProductFoundMessage: "<p>No se ha encontrado ningún dispositivo. Asegúrese de que la smartcard o el token están conectados e inténtelo de nuevo.<p/>"
      + "<div>Si el problema persiste, póngase en contacto con el servicio de asistencia.</div>",
    getUrlError: "Error al obtener los archivos NexU por la URL especificada.",
    getSealError: "Error al obtener la lista de sellos."
  },

  nexuLoading: {
    checkVersion: "Comprobando la versión de NexU.",
    getSmart: "Obtener certificados.",
    getTbs: "Obtención de datos para la firma.",
    finalize: "Proceso de acabado.",
    chooseCert: "Debes elegir un certificado asociado a una clave privada para firmar.",
    sigError: "<p>Ha ocurrido un error en el proceso de firma con la smartcard/token. Por favor, inténtelo de nuevo,</p>"
      + "<div>Si el problema persiste, póngase en contacto con el servicio de asistencia.</div>",
    storeNotification: "Almacenando notificación."
  },

  trustedListFactory: {
    getTlError: "La Lista de Confianza solicitada no existe.",
    getSignatureError: "Error al cargar la información de la firma.",
    getChangeError: "Error al cargar los cambios.",
    getCheckError: "Error en la carga de la inspección.",
    getAllDraftsError: "Error al obtener borradores.",
    getAllCheckError: "Error al obtener inspecciones.",
    getDraftEditionCheckError: "Error al habilitar las inspecciones.",
    checkProviderError: "Fallo en las inspecciones realizadas en el proveedor de servicios de confianza.",
    getLastEditedError: "Error al cargar la última fecha editada.",
    conflictTlFailure: "Error al resolver los conflictos de edición de la Lista de Confianza.",
    checkSignatureFailure: "Fallo al verificar la firma.",
    notifiedPointerFailure: "Fallo en la obtención de notificaciones.",
    switchCheckToRunError: "Error al conmutar la ejecución de la inspección.",
    tlIdIncorrect: "El identificador de la Lista de Confianza de la URL es incorrecto. Introduzca un número natural o un código de país.",
    getChangesDraftError: "Se ha producido un error al obtener los cambios.",
    signatureChangeError: "Se ha producido un error al obtener los cambios de firma."
  },

  cookieFactory: {
    validityError: "El repositorio solicitado no existe.",
    validityFailure: "Fallo en la comprobación de la validez del repositorio.",
    createFailure: "Fallo en la creación de un nuevo repositorio.",
    createCookie: "<div> <label><u>Creación de un repositorio de borradores de Listas de Confianza</u></label></div>"
      + "<div>Se ha creado para usted un repositorio personal de borradores de la Lista de Confianza en la carpeta <b>“Mis borradores”</b>. "
      + "Este repositorio almacenará todos los borradores creados por usted.</div>"
      + "<div>Se ha generado un identificador único para ese repositorio. "
      + "Este identificador puede verse en la URL de la página, en la barra de direcciones de su navegador.</div><br/>"
      + "<div> <label><u>Cómo acceder</u></label></div>"
      + "<p>Para su comodidad, un <b><i>cookie</i></b> se ha configurado en este navegador, que remite al repositorio “Mis borradores”. "
      + "Así, cuando vuelvas a la aplicación, encontrarás el mismo repositorio con tus borradores en él.</p>"
      + "<div>No obstante, tenga en cuenta que <i>cookies</i> puede ser eliminado por un eventual mantenimiento de su PC. "
      + "Por lo tanto, se recomienda encarecidamente que usted <u>favorite</u> esta página para futuras consultas.</div>"
      + "<div>Otra forma de recordar la página es copiar y pegar la URL en un documento, correo electrónico u otro medio de almacenar información textual.</div>"
      + "<div>Con esta URL, puedes:</div>"
      + "<div class='alinea'>-  <b>Accende</b> al mismo repositorio en el futuro y encuentra los borradores que has creado.</div>"
      + "<div class='alinea'>-  <b>Comparte</b> tu repositorio con otras personas (por ejemplo, compañeros de trabajo) enviándoles la URL.</div><br/>"
      + "<div> <label><u>Compartir un repositorio</u></label></div>"
      + "<p>Compartir su repositorio con otras personas puede ser una forma sencilla de colaborar en el contenido de una nueva Lista de Confianza antes de publicarla. "
      + "Sin embargo, si has compartido tu repositorio, ten en cuenta que cualquiera que conozca la URL puede <b>navegar, editar, eliminar y firmar</b> tus borradores. "
      + "Así pues, si tiene previsto publicar un borrador como Lista de Confianza oficial de su país, se recomienda encarecidamente que guarde una copia local en su PC.  "
      + "De este modo, puede evitar que otros modifiquen accidental o secretamente su Lista de Confianza justo antes de firmarla y publicarla.</p>"
      + "<p>Si quieres <b>dejar de compartir</b> tu repositorio con otros, se sugiere generar un nuevo repositorio (ver el botón en la página “Mis borradores”). "
      + "Las personas podrán seguir accediendo al antiguo repositorio compartido, pero usted trabajarás en un nuevo repositorio personal.</p>"
      + "<div><label><u>Política de retención</u></label></div>"
      + "<div>El repositorio se mantendrá durante <b>2 (dos) meses</b> después de la eliminación del último borrador de la Lista de Confianza en él. "
      + "Mientras el repositorio contenga al menos un borrador, seguirá estando accesible (observe la política de retención de Listas de Confianza).</div>",
    replaceStore: "<div>Según la información presente en la <i>cookie</i>, parece que el repositorio al que intenta acceder no es su repositorio habitual de “Mis borradores”. "
      + "Haga clic en “Aceptar” si desea reemplazar su repositorio con el designado en esta URL.</div>"
      + "<div>Hacer clic en “Cancelar” evitará esta acción, por lo que puede, por ejemplo, marcar su repositorio actual para usarlo en el futuro antes de cambiar al nuevo.</div>",
    invalidUrl: "El repositorio “Mis borradores” al que intenta acceder ha caducado.",
    invalidDraftStore: "Tu repositorio habitual de “Mis borradores” al que intentas acceder ha caducado.",
    newStore: "<div>Esta operación creará un nuevo repositorio y abandonará el actual. "
      + "Las personas con las que <b>compartiste</b> el repositorio actual aún podrán acceder a él, pero ahora estarás trabajando en un nuevo repositorio personal <b>vacío</b>.</div> "
      + "Si desea acceder al repositorio actual en el futuro, <b>marque</b> esta página antes de seguir. "
      + "Si desea transferir algunos de sus borradores al nuevo repositorio, <b>expórtelos</b> primero e impórtelos al nuevo repositorio después de la creación."
      + "<p>¿Quieres <b>continuar</b> de todos modos?</p>",
    repository: "Información del repositorio"
  },

  httpStatus: {
    notAuthorized: "No está autorizado para realizar la operación actual o su sesión ha expirado.",
    tlNotFound: "No se encontró el archivo de Lista de Confianza. Si el problema persiste, póngase en contacto con el servicio de asistencia.",
    titleApplicationError: "Error de aplicación",
    titleNotAuthorized: "No autorizado",
    titleNotFound: "No encontrado",
    titleBadRequest: "Solicitud incorrecta",
    titleProhibido: "Prohibido",
    titleError: "Error"
  },

  modalTitle: {
    warning: "Advertencia",
    applicationError: "Error de aplicación",
    signatureError: "Error de firma",
    information: "Información",
    repositorio: "Información del repositorio",
    confirmation: "Confirmación"
  },

  pointerController: {
    digitalDouble: "Certificado ya existe en identidades digitales: ",
    digitalMultipleDouble: "Los certificados ya existen en las identidades digitales: ",
    getDigital: "Esta operación reemplazará las identidades digitales existentes con certificados de firmante, ¿desea continuar?",
    getDigitalError: "Error al obtener las identidades digitales."
  },

  systemController: {
    loadingFailure: "Error al cargar.",
    rulesFailure: "Error en la validación de las reglas.",
    signatureFailure: "Falló la verificación de la firma.",
    retentionFailure: "Error en la ejecución de la política de retención.",
    serviceDataFailure: "Error al recargar los datos del servicio.",
    signatureAlertFailure: "Error al recibir las advertencias de firma.",
    cacheCountriesFailure: "Falló la limpieza del caché del los países.",
    cachePropertiesFailure: "Error al borrar la caché de propiedades.",
    cacheCheckFailure: "Error al borrar el caché de inspecciones.",
    cacheCountriesSuccess: "Limpieza de la caché del los países realizada correctamente.",
    cachePropertiesSuccess: "Limpieza de caché de propiedades realizada correctamente.",
    cacheChecksSuccess: "Limpieza del caché de inspección realizado correctamente."
  },

  userController: {
    userAdded: "Nombre de usuario “%NAME%” añadido.",
    updateSuccessful: "Actualización realizada con éxito."
  },

  retentionController: {
    loading_data: "Cargando informaciónes...",
    loading_failure: "Fallo al cargar la información.",
    delete_draftstore_failure: "Fallo en la eliminación de datos."
  },

});
