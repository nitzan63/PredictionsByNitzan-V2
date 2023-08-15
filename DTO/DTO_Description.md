
| DTO | Command | Data Held | Initialized by | Transferred to |
|-----|---------|-----------|----------------|----------------|
| EntitiesDefinitionDTO | 2 | Details about entities (name, population, properties) | Engine | UI |
| RuleDTO | 2 | Details about rules (name, activation, actions) | Engine | UI |
| TerminationDTO | 2 | Details about termination conditions (by ticks or by seconds) | Engine | UI |
| UserEnvironmentInputDTO | 3 | User's input for environment properties | UI | Engine |
| SimulationRunMetadataDTO | 3, 4 | Metadata about the simulation run (run identifier, date and time of run) | Engine | UI |
| SimulationRunResultsDTO | 3, 4 | Results of the simulation run (initial and final population, property histograms) | Engine | UI |
| EnvironmentDTO | 3 | Details about environment properties | Engine | UI |
| PopulationStatisticsDTO | 4 | Initial and final population statistics | Engine | UI |
| PropertyHistogramDTO | 4 | Property histograms (how many instances have each property value) | Engine | UI |

