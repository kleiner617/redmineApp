(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Project', 'Issue'];

    function ProjectDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Project, Issue) {
        var vm = this;

        vm.project = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('redmineappApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
