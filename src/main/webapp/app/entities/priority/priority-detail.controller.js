(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('PriorityDetailController', PriorityDetailController);

    PriorityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Priority', 'Issue'];

    function PriorityDetailController($scope, $rootScope, $stateParams, previousState, entity, Priority, Issue) {
        var vm = this;

        vm.priority = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('redmineappApp:priorityUpdate', function(event, result) {
            vm.priority = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
