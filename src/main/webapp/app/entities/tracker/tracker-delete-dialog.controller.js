(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('TrackerDeleteController',TrackerDeleteController);

    TrackerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tracker'];

    function TrackerDeleteController($uibModalInstance, entity, Tracker) {
        var vm = this;

        vm.tracker = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tracker.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
