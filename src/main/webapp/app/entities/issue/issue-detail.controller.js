(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('IssueDetailController', IssueDetailController);

    IssueDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Issue', 'Project', 'Tracker', 'Status', 'Priority', 'Author', 'FixedVersion'];

    function IssueDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Issue, Project, Tracker, Status, Priority, Author, FixedVersion) {
        var vm = this;

        vm.issue = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('redmineappApp:issueUpdate', function(event, result) {
            vm.issue = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
