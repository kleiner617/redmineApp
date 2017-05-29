(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('IssueDialogController', IssueDialogController);

    IssueDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Issue', 'Project', 'Tracker', 'Status', 'Priority', 'Author', 'FixedVersion'];

    function IssueDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Issue, Project, Tracker, Status, Priority, Author, FixedVersion) {
        var vm = this;

        vm.issue = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.projects = Project.query();
        vm.trackers = Tracker.query();
        vm.statuses = Status.query();
        vm.priorities = Priority.query();
        vm.authors = Author.query();
        vm.fixedversions = FixedVersion.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.issue.id !== null) {
                Issue.update(vm.issue, onSaveSuccess, onSaveError);
            } else {
                Issue.save(vm.issue, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('redmineappApp:issueUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.createdOn = false;
        vm.datePickerOpenStatus.updatedOn = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
