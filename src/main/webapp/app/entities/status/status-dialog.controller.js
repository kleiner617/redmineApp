(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('StatusDialogController', StatusDialogController);

    StatusDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Status', 'Issue'];

    function StatusDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Status, Issue) {
        var vm = this;

        vm.status = entity;
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
            if (vm.status.id !== null) {
                Status.update(vm.status, onSaveSuccess, onSaveError);
            } else {
                Status.save(vm.status, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('redmineappApp:statusUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
