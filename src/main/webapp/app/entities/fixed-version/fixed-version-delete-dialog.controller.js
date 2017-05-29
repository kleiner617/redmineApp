(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .controller('FixedVersionDeleteController',FixedVersionDeleteController);

    FixedVersionDeleteController.$inject = ['$uibModalInstance', 'entity', 'FixedVersion'];

    function FixedVersionDeleteController($uibModalInstance, entity, FixedVersion) {
        var vm = this;

        vm.fixedVersion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FixedVersion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
