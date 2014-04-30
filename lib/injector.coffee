injector = require('inject-it')()

injector.value('pkg', require('../package.json'))

# third part libraries
injector.value('React', require('react/addons'))
injector.value('_', require('lodash'))
injector.value('$', require('jquery'))
injector.value('observeit', require('observe-it'))
injector.value('W', require('when'))
injector.value('nodefn', require('when/node'))
injector.value('uuid', require('node-uuid'))

# local libraries
injector.factory('arrayMove', require('./util/array_move.coffee'))
injector.factory('JsonStore', require('./util/json_store.coffee'))

# controllers
injector.service('SearchController', require('./controllers/search_controller.coffee'))
injector.service('SettingsController', require('./controllers/settings_controller.coffee'))

# components
injector.factory('Search', require('./jsx/components/search.jsx'))
injector.factory('ExternalLink', require('./jsx/components/external-link.jsx'))
injector.factory('MultiPage', require('./jsx/components/multipage.jsx'))
injector.factory('MultiSelect', require('./jsx/components/multiselect.jsx'))
injector.factory('FileDropper', require('./jsx/components/file_dropper.jsx'))
injector.factory('Updater', require('./jsx/components/updater.jsx'))

# pages
injector.factory('SearchPage', require('./jsx/pages/search-page.jsx'))
injector.factory('SettingsPage', require('./jsx/pages/settings-page.jsx'))

# app root
injector.factory('SubtitleMaster', require('./jsx/subtitle-master.jsx'))

# others
injector.value('languages', require('subtitle-master/lib/languages.coffee'))
injector.value('localStorage', window.localStorage)

module.exports = injector
