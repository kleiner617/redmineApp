(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('priority', {
            parent: 'entity',
            url: '/priority?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'redmineappApp.priority.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/priority/priorities.html',
                    controller: 'PriorityController',
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
                    $translatePartialLoader.addPart('priority');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('priority-detail', {
            parent: 'entity',
            url: '/priority/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'redmineappApp.priority.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/priority/priority-detail.html',
                    controller: 'PriorityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('priority');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Priority', function($stateParams, Priority) {
                    return Priority.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'priority',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('priority-detail.edit', {
            parent: 'priority-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/priority/priority-dialog.html',
                    controller: 'PriorityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Priority', function(Priority) {
                            return Priority.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('priority.new', {
            parent: 'priority',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/priority/priority-dialog.html',
                    controller: 'PriorityDialogController',
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
                    $state.go('priority', null, { reload: 'priority' });
                }, function() {
                    $state.go('priority');
                });
            }]
        })
        .state('priority.edit', {
            parent: 'priority',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/priority/priority-dialog.html',
                    controller: 'PriorityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Priority', function(Priority) {
                            return Priority.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('priority', null, { reload: 'priority' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('priority.delete', {
            parent: 'priority',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/priority/priority-delete-dialog.html',
                    controller: 'PriorityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Priority', function(Priority) {
                            return Priority.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('priority', null, { reload: 'priority' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
