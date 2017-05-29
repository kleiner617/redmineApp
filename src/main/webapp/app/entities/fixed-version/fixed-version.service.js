(function() {
    'use strict';
    angular
        .module('redmineappApp')
        .factory('FixedVersion', FixedVersion);

    FixedVersion.$inject = ['$resource'];

    function FixedVersion ($resource) {
        var resourceUrl =  'api/fixed-versions/:id';

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
