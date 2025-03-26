package tutorly.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import tutorly.commons.core.index.Index;
import tutorly.commons.util.ToStringBuilder;
import tutorly.logic.Messages;
import tutorly.logic.commands.exceptions.CommandException;
import tutorly.model.Model;
import tutorly.model.person.Person;
import tutorly.ui.Tab;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteStudentCommand extends StudentCommand {

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_STRING = StudentCommand.COMMAND_STRING + " " + COMMAND_WORD;

    public static final String MESSAGE_USAGE = COMMAND_STRING
            + ": Deletes and archive the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_STRING + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private final Index targetIndex;

    public DeleteStudentCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(personToDelete);
        return new CommandResult.Builder(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)))
                .withTab(Tab.STUDENT)
                .build();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteStudentCommand)) {
            return false;
        }

        DeleteStudentCommand otherDeleteCommand = (DeleteStudentCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
