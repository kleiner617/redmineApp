(function() {
    'use strict';
    angular
        .module('redmineappApp')
        .factory('Tracker', Tracker);

    Tracker.$inject = ['$resource'];

    function Tracker ($resource) {
        var resourceUrl =  'api/trackers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
