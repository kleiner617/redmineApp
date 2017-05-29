(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('TrackerDetailController', TrackerDetailController);

    TrackerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tracker', 'Issue'];

    function TrackerDetailController($scope, $rootScope, $stateParams, previousState, entity, Tracker, Issue) {
        var vm = this;

        vm.tracker = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('redmineappApp:trackerUpdate', function(event, result) {
            vm.tracker = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
