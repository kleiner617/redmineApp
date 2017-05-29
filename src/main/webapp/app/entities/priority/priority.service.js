(function() {
    'use strict';
    angular
        .module('redmineappApp')
        .factory('Priority', Priority);

    Priority.$inject = ['$resource'];

    function Priority ($resource) {
        var resourceUrl =  'api/priorities/:id';

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
