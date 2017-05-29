(function() {
    'use strict';
    angular
        .module('redmineappApp')
        .factory('Issue', Issue);

    Issue.$inject = ['$resource', 'DateUtils'];

    function Issue ($resource, DateUtils) {
        var resourceUrl =  'api/issues/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.createdOn = DateUtils.convertDateTimeFromServer(data.createdOn);
                        data.updatedOn = DateUtils.convertDateTimeFromServer(data.updatedOn);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
