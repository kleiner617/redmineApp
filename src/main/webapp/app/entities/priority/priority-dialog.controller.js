(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('PriorityDialogController', PriorityDialogController);

    PriorityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Priority', 'Issue'];

    function PriorityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Priority, Issue) {
        var vm = this;

        vm.priority = entity;
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
            if (vm.priority.id !== null) {
                Priority.update(vm.priority, onSaveSuccess, onSaveError);
            } else {
                Priority.save(vm.priority, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('redmineappApp:priorityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
