CREATE SCHEMA IF NOT EXISTS gestion_contrats AUTHORIZATION dev_user;
INSERT INTO gestion_contrats.offre (nom_offre, description_min, description_max, prix_min, prix_max) VALUES
                                                                                                         ('SansAcc-SansMoyTra',
                                                                                                          'Conseils médicaux\nOrientation vers des hôpitaux référencés',
                                                                                                          'Conseils médicaux\nOrientation vers des hôpitaux référencés\nPrise en charge des frais médicaux\nRapatriement',
                                                                                                          '20€/mois',
                                                                                                          '50€/mois'),

                                                                                                         ('SansAcc-AvecMoyTra',
                                                                                                          'Dépannage\nRemorquage\nConseils médicaux\nOrientation vers des hôpitaux référencés',
                                                                                                          'Dépannage\nRemorquage\nAssistance technique et diagnostique\nConseils médicaux\nOrientation vers des hôpitaux référencés\nPrise en charge des frais médicaux\nRapatriement',
                                                                                                          '40€/mois',
                                                                                                          '60€/mois'),

                                                                                                         ('AvecAcc-SansMoyTra',
                                                                                                          'Conseils médicaux\nOrientation vers des hôpitaux référencés',
                                                                                                          'Conseils médicaux\nOrientation vers des hôpitaux référencés\nPrise en charge des frais médicaux\nRapatriement',
                                                                                                          '25€/mois/personneAccompagnante',
                                                                                                          '70€/mois/personneAccompagnante'),

                                                                                                         ('AvecAcc-AvecMoyTra',
                                                                                                          'Dépannage\nRemorquage\nConseils médicaux\nOrientation vers des hôpitaux référencés',
                                                                                                          'Dépannage\nRemorquage\nAssistance technique et diagnostique\nConseils médicaux\nOrientation vers des hôpitaux référencés\nPrise en charge des frais médicaux\nRapatriement',
                                                                                                          '65€/mois/personneAccompagnante',
                                                                                                          '130€/mois/personneAccompagnante');
