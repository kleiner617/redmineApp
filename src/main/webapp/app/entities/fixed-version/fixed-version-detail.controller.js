(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('FixedVersionDetailController', FixedVersionDetailController);

    FixedVersionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FixedVersion', 'Issue'];

    function FixedVersionDetailController($scope, $rootScope, $stateParams, previousState, entity, FixedVersion, Issue) {
        var vm = this;

        vm.fixedVersion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('redmineappApp:fixedVersionUpdate', function(event, result) {
            vm.fixedVersion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
