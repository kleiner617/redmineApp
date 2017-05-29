(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('PriorityDeleteController',PriorityDeleteController);

    PriorityDeleteController.$inject = ['$uibModalInstance', 'entity', 'Priority'];

    function PriorityDeleteController($uibModalInstance, entity, Priority) {
        var vm = this;

        vm.priority = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Priority.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
