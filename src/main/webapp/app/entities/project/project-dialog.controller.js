(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('ProjectDialogController', ProjectDialogController);

    ProjectDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Project', 'Issue'];

    function ProjectDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Project, Issue) {
        var vm = this;

        vm.project = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
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
            if (vm.project.id !== null) {
                Project.update(vm.project, onSaveSuccess, onSaveError);
            } else {
                Project.save(vm.project, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('redmineappApp:projectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdOn = false;
        vm.datePickerOpenStatus.updatedOn = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
