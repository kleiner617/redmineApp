(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('TrackerDialogController', TrackerDialogController);

    TrackerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tracker', 'Issue'];

    function TrackerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Tracker, Issue) {
        var vm = this;

        vm.tracker = entity;
        vm.clear = clear;
        vm.save = save;
        vm.issues = Issue.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tracker.id !== null) {
                Tracker.update(vm.tracker, onSaveSuccess, onSaveError);
            } else {
                Tracker.save(vm.tracker, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('redmineappApp:trackerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
