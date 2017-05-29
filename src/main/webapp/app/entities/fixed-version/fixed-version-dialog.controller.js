(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('FixedVersionDialogController', FixedVersionDialogController);

    FixedVersionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FixedVersion', 'Issue'];

    function FixedVersionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FixedVersion, Issue) {
        var vm = this;

        vm.fixedVersion = entity;
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
            if (vm.fixedVersion.id !== null) {
                FixedVersion.update(vm.fixedVersion, onSaveSuccess, onSaveError);
            } else {
                FixedVersion.save(vm.fixedVersion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('redmineappApp:fixedVersionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
