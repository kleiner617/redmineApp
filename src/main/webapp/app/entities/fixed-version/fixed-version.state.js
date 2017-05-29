(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fixed-version', {
            parent: 'entity',
            url: '/fixed-version?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'redmineappApp.fixedVersion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fixed-version/fixed-versions.html',
                    controller: 'FixedVersionController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fixedVersion');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('fixed-version-detail', {
            parent: 'entity',
            url: '/fixed-version/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'redmineappApp.fixedVersion.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fixed-version/fixed-version-detail.html',
                    controller: 'FixedVersionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fixedVersion');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FixedVersion', function($stateParams, FixedVersion) {
                    return FixedVersion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'fixed-version',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('fixed-version-detail.edit', {
            parent: 'fixed-version-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fixed-version/fixed-version-dialog.html',
                    controller: 'FixedVersionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FixedVersion', function(FixedVersion) {
                            return FixedVersion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fixed-version.new', {
            parent: 'fixed-version',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fixed-version/fixed-version-dialog.html',
                    controller: 'FixedVersionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('fixed-version', null, { reload: 'fixed-version' });
                }, function() {
                    $state.go('fixed-version');
                });
            }]
        })
        .state('fixed-version.edit', {
            parent: 'fixed-version',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fixed-version/fixed-version-dialog.html',
                    controller: 'FixedVersionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FixedVersion', function(FixedVersion) {
                            return FixedVersion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fixed-version', null, { reload: 'fixed-version' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fixed-version.delete', {
            parent: 'fixed-version',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fixed-version/fixed-version-delete-dialog.html',
                    controller: 'FixedVersionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FixedVersion', function(FixedVersion) {
                            return FixedVersion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fixed-version', null, { reload: 'fixed-version' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
